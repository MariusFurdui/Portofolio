<?php
$host = 'localhost';
$dbname = 'colocviu';
$username = 'root';
$password = '';

try {
    $conn = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8mb4", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $conn->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
    
} catch(PDOException $e) {
    die("Eroare conexiune MySQL: " . $e->getMessage());
}

function formatPrice($price, $currency) {
    $formatted = number_format($price, 0, ',', '.');
    $symbols = ['EUR' => '€', 'RON' => 'RON', 'USD' => '$'];
    $symbol = $symbols[$currency] ?? $currency;
    return $formatted . ' ' . $symbol;
}

function getOfferType($type) {
    return $type == 'D' ? 'Vânzare' : 'Închiriere';
}
?>