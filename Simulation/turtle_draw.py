import turtle
from math import atan2, degrees, hypot
from time import sleep
from sys import argv, exit
from ftc_field import *

if len(argv) <= 1:
    print("Supply path to script")
    exit(1)

setup_playing_field()

def move(draw, args):
    x, y = map(float, args)
    deg = 90 - degrees(atan2(y, x))
    if deg > 0:
        draw.right(deg)
    else:
        draw.left(-deg)
    draw.forward(hypot(x, y))
    if deg > 0:
        draw.left(deg)
    else:
        draw.right(-deg)

cmds = {
    'move': move,
    'rotate': lambda draw, args: draw.right(int(args[0]))
}

starts = [(34.5/2, 34.5/2, 45), (-34.5/2, 34.5/2, 135)]

try:
    scripts = []
    for filename in argv[1:]:
        with open(filename) as f:
            scripts.append(f.readlines())

    starter = 0
    for script in scripts:
        obj = turtle.Turtle()
        setup_turtle(obj, starts[starter % len(starts)])
        starter += 1
        for task in script:
            tokens = task.strip().split(' ')
            if tokens[0] in cmds:
                cmds[tokens[0]](obj, tokens[1:])
                print(task.strip())
                sleep(0.2)
            else:
                print(f'running custom task: {task.strip()}')
                obj.color('#ff0000')
                sleep(2)
                obj.color('#ffff00')

except FileNotFoundError:
    print("No such file")
except Exception as e:
    print(e)

input('<enter> to continue')
turtle.done()