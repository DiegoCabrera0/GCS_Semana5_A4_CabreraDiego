<?php
$passwords = [
    'admin123' => password_hash('admin123', PASSWORD_BCRYPT),
    'admin' => password_hash('admin', PASSWORD_BCRYPT),
    'profesor123' => password_hash('profesor123', PASSWORD_BCRYPT),
    'alumno123' => password_hash('alumno123', PASSWORD_BCRYPT),
    'rep123' => password_hash('rep123', PASSWORD_BCRYPT)
];

echo "Hashes generados:\n";
foreach ($passwords as $plain => $hash) {
    echo "'$plain' => '$hash'\n";
}
