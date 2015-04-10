# rabbitmq
RabbitMQ Tutorial

http://www.rabbitmq.com/tutorials/tutorial-two-java.html
=> Work Queues

Round Robin Dispatching:
Workers can work in parallel.
Workers can be added easily, so we can scale. The Tasks (Publisher) will be thus sent to multiple consumers.

To Test, start several Worker Threads and then run NewTask several times.

Using message acknowledgments and prefetchCount you can set up a work queue. The durability options let the tasks survive even if RabbitMQ is restarted.
