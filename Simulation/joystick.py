import pygame

pygame.init()
pygame.joystick.init()
pygame.joystick.Joystick(0).init()

def poll_events():
    return list(
        filter(
            lambda x: x.type == pygame.JOYAXISMOTION and x.axis <3,
            pygame.event.get()))