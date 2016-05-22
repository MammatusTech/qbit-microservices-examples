
echo "Todo item list before "
curl http://localhost:8080/todo-service/todo/
echo

echo "Count of Todo items "
curl http://localhost:8080/todo-service/todo/count
echo

echo "PUT a TODO item"
curl -X PUT http://localhost:8080/todo-service/todo/ -H 'Content-Type: application/json' \
-d '{"name":"wash-car", "description":"Take the car to the car wash", "createTime":1463950095000}'
echo


echo "Todo item list after add "
curl http://localhost:8080/todo-service/todo/
echo

echo "Count of Todo items after add "
curl http://localhost:8080/todo-service/todo/count
echo

echo "Remove a TODO item"
curl -X DELETE http://localhost:8080/todo-service/todo/?id=wash-car::1463950095000
echo


echo "Todo item list after add "
curl http://localhost:8080/todo-service/todo/
echo

echo "Count of Todo items after add "
curl http://localhost:8080/todo-service/todo/count
echo