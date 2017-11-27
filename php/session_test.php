<?php
  if (!isset($_GET['sid'])) {
    echo 'Session id not provided.';
    exit();
  }
  $sid = $_GET['sid'];
  session_id($sid);
  session_start();
  echo json_encode($_SESSION);

?>
