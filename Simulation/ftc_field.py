import turtle

def setup_playing_field(obj):
    screen = obj.getscreen()
    screen.setworldcoordinates(-12*6, -12*6, 12*6, 12*6)
    screen.screensize(720, 720)
    screen.bgpic('field.gif')

def setup_turtle(obj, start):
    obj.hideturtle()
    obj.speed(3)
    obj.shape('triangle')
    obj.turtlesize(4.5, 4.5)
    obj.penup()
    # obj.setpos(-34.5/2, 34.5/2)
    obj.setpos(start[0], start[1])
    obj.pendown()
    obj.setheading(start[2])
    obj.width(3)
    obj.color('#ffff00')
    obj.showturtle()