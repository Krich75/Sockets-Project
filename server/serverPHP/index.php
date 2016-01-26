<?php

function getValue($message) {
	$result = 0;
	
	foreach (count_chars(strtolower($message), 0) as $i => $val) {
		$result += $i >= 97 ? $i <= 122 ? ($i - 97 + 1) * $val : 0 : 0;
	}
	
	return $result;
}

function getCharactersNumber($message) {
	$result = 0;
	
	foreach (count_chars($message, 0) as $i => $val) {
		$result += $i !== 32 ? $val : 0;
	}
	
	return $result;
}

function process($var){
	if (isset($var['action'], $var['text'])) {
		switch ($var['action']) {
			case 'val':
				echo getValue($var['text']);
			break;
			
			case 'nbc':
				echo getCharactersNumber($var['text']);
			break;
			
			default:
				header('HTTP/1.1 400 Bad Request');
				header('Content-Type: text/plain');
				echo 'HTTP/1.1 400 Bad Request';
			break;
		}
	} else {
		header('HTTP/1.1 400 Bad Request');
		header('Content-Type: text/plain');
		echo 'HTTP/1.1 400 Bad Request';
	}
}

if (isset($_SERVER['REQUEST_METHOD'])) {
	switch ($_SERVER['REQUEST_METHOD']) {
		case 'GET':
			process($_GET);
		break;
			
		case 'POST':
			process($_POST);
		break;
		
		default:
			header('HTTP/1.1 405 Method Not Allowed');
			header('Content-Type: text/plain');
			echo 'HTTP/1.1 405 Method Not Allowed';
		break;
	}
} else {
	header('HTTP/1.1 500 Internal Server Error');
	header('Content-Type: text/plain');
	echo 'HTTP/1.1 500 Internal Server Error';
}
