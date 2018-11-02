#include <SFML/Window.hpp>
#include <SFML/Graphics.hpp>
#include <iostream>

int main() {
    sf::RenderWindow window(sf::VideoMode(720, 720), "TeleOp Driver Training");
    sf::Texture texture;
    sf::Sprite sprite;
    sf::RectangleShape robot(sf::Vector2f(18*5.f, 18*5.f));

    robot.setFillColor(sf::Color(255, 255, 0));
    texture.loadFromFile("field.png");
    sprite.setTexture(texture);

    while (window.isOpen()) {
        // check all the window's events that were triggered since the last iteration of the loop
        sf::Event event;
        while (window.pollEvent(event))
        {
            // "close requested" event: we close the window
            if (event.type == sf::Event::Closed)
                window.close();
        }

        window.clear();
        window.draw(sprite);
        window.draw(robot);
        window.display();
    }
}