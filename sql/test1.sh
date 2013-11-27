echo 'start the script'
echo 'inside sudo'
mysql --host=localhost --user=testuser --password=testpass testdb << EOF
source testsql.sql;
EOF
