$url = "https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar"
$output = "lib/mysql-connector-java-8.0.33.jar"

# Create lib directory if it doesn't exist
if (-not (Test-Path "lib")) {
    New-Item -ItemType Directory -Path "lib"
}

# Download the file
Invoke-WebRequest -Uri $url -OutFile $output

Write-Host "MySQL JDBC driver downloaded successfully to $output" 