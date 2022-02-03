#this will copy a file to destination directory
$filePath=$args[0]
$destination=$args[1]
Copy-Item $filePath -Destination $destination