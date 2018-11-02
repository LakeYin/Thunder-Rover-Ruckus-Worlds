import ftc_field

turtle = ftc_field.turtle.Turtle()
ftc_field.setup_turtle(turtle, [34.5/2, 34.5/2, 45])
ftc_field.setup_playing_field(turtle)

import joystick

dx = 0
dy = 0
dh = 0

turtle.setheading(90)
turtle.penup()
turtle.turtlesize(4.5, 4.5)

while True:
    for event in joystick.poll_events():
        if event.axis == 0:
            dx = event.value
        elif event.axis == 1:
            dy = event.value
        else:
            dh = event.value
    
    turtle.setpos(turtle.pos() + (dx, -dy))
    turtle.setheading(turtle.heading() - dh*10)