# Creature Simulator
This simulation uses the Box2D physics engine to simulate the physics mechanics. JavaFX was used as the graphics library. Genetic neural networks were used to control the robots. The neural networks take in inputs like the leg joint angles, leg joint speeds, body rotation, height above ground and whether it is touching the ground. It outputs the four motor joint speeds. There are 50 robots with different neural networks. The best performing robot is selected. Its neural network is cloned 50 times and the weights and biases are randomly tweaked and tested again with 50 new robots in the next generation.
## Youtube
https://www.youtube.com/watch?v=LGi9LeGDtrI
## References
This project idea is from Code Bullet (https://www.youtube.com/watch?v=K-wIZuAA3EY). I created my own version to improve upon his algorithm. While I took the idea of using the Box2D physics engine and the aesthetics of program from Code Bullet's video, I wrote the code without referencing his code.
