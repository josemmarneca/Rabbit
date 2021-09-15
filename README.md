# Rabbit
Rabbit consume and send messages

send:
curl -X POST \
  http://localhost:8081/rabbitmq/producer \
  -H 'content-type: application/json' \
  -d '{
	"content": "Minha primeira messagem",
	"sender": "API",
	"type": "CHAT"
}'

consume:
curl -X GET \
http://localhost:8081/rabbitmq/consume
