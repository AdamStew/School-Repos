<?php
/**
 * Created by PhpStorm.
 * User: Adam
 * Date: 10/2/2017
 * Time: 5:09 PM
 */


$db = new mysqli('localhost', 'stewarad', 'stewarad', 'hw4DB');

// sql to create table
$sql = "CREATE TABLE friend (
id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY, 
name VARCHAR(30) NOT NULL,
phone VARCHAR(10) NOT NULL,
age INT(6)
)";

?>