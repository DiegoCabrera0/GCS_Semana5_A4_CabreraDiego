<?php

namespace App\Security;

use Exception;

class JwtManager {
    private static function base64UrlEncode(string $data): string {
        return rtrim(strtr(base64_encode($data), '+/', '-_'), '=');
    }

    private static function base64UrlDecode(string $data): string {
        return base64_decode(strtr($data, '-_', '+/') . str_repeat('=', (4 - strlen($data) % 4) % 4));
    }

    public static function encode(array $payload, string $secret, int $ttl = 28800): string {
        $header = ['typ' => 'JWT', 'alg' => 'HS256'];
        $now = time();
        $payload['iat'] = $now;
        $payload['exp'] = $now + $ttl;

        $base64UrlHeader = self::base64UrlEncode(json_encode($header));
        $base64UrlPayload = self::base64UrlEncode(json_encode($payload));

        $signature = hash_hmac('sha256', $base64UrlHeader . "." . $base64UrlPayload, $secret, true);
        $base64UrlSignature = self::base64UrlEncode($signature);

        return $base64UrlHeader . "." . $base64UrlPayload . "." . $base64UrlSignature;
    }

    public static function decode(string $jwt, string $secret): ?array {
        $tokenParts = explode('.', $jwt);
        if (count($tokenParts) !== 3) {
            return null;
        }

        [$headerB64, $payloadB64, $sigB64] = $tokenParts;

        $signature = self::base64UrlEncode(hash_hmac('sha256', $headerB64 . "." . $payloadB64, $secret, true));
        if (!hash_equals($signature, $sigB64)) {
            return null;
        }

        $payload = json_decode(self::base64UrlDecode($payloadB64), true);
        if (!$payload || !isset($payload['exp']) || $payload['exp'] < time()) {
            return null;
        }

        return $payload;
    }
}
