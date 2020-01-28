<?php
// Implémenter l'opération = ?
function sayhello($name){
    return "hello " . $name;
}

// Création du serveur
$server = new SoapServer("helloworld.wsdl");
// Ajout de l'opération
$server->addFunction("sayhello") ;
$server->handle();

?>
