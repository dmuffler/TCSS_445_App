<?php

// connect to the Database
$dsn = 'mysql:host=localhost;dbname=dmuffler';
$username = 'dmuffler';
$password = 'Dawckyun';

try {
  $db = new PDO($dsn, $username, $password);
} catch(PDOException $e) {
    $error_message = $e->getMessage();
    $result = array("code" => 300, "message" => "There was an error connecting to the database: $error_message");
    echo json_encode($result);
    exit();
}

?>
