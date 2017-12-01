<?php
ini_set('display_errors', '1');
error_reporting(E_ALL);

// connect to the Database
$dsn = 'mysql:host=localhost;dbname=dmuffler';
$username = 'dmuffler';
$password = 'Dawckyun';

// 0 if a student, else an admin.
$control = $_GET['my_control'];

$email = $_GET['my_email'];
$pass = $_GET['my_pass'];
$first_name = $_GET['my_first_name'];
$last_name = $_GET['my_last_name'];
$account_check;
$account_add;

try {
    #make a new DB object to interact with
    $db = new PDO($dsn, $username, $password);
    // user register
    if ($control == 0) {
        #build a SQL statement to query the DB
        $account_check = "SELECT email FROM Student WHERE email = ?";
    // admin register
    } else {
        #build a SQL statement to query the DB
        $account_check = "SELECT email FROM Admin WHERE email = ?";
    }
    
    #make a query object
    $user_query = $db->prepare($account_check);
    $user_query->execute(array($email));
    
    // run the query on the DB
    $users = $user_query->fetchAll(PDO::FETCH_ASSOC);
    
    if ($users) {
        //user exists
        print "false";
    } else {
        // user does not exist
        if ($control == 0) {
            $account_add = "INSERT INTO Student VALUES (?, SHA(?), ?, ?)";
        } else {
            $account_add = "INSERT INTO Admin VALUES (?, SHA(?), ?, ?)";
        }
        $arguments = array($email, $pass, $first_name, $last_name);
        $prepared_statement = $db->prepare($account_add);
        $result = $prepared_statement->execute($arguments);
        print ($result ? "true" : "false");
    }
   // echo json_encode($result);
    $db = null;
} catch (PDOException $e) {
    $error_message = $e->getMessage();
    $result = array("code"=>300, "message"=>"There was an error connecting to the database: $error_message");
    echo json_encode($result);
    exit();
}
?>
