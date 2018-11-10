import hlt # Import Halite Starter-kit.
import logging # Import logging module.
from collections import OrderedDict

#GAME START
game = hlt.Game("Settler4000")
logging.info("Starting my Settler4000 bot!")

planned_planets = []  # Keep track of what planets we're traveling to.
first_turn = True  # Determine if it's the first turn of the match.

while True:
    # TURN START  
    game_map = game.update_map()  # Update the map for the new turn and get the latest version
    command_queue = []  # Keep track of commands.
    my_planets = []  # Keep track of what planets I own.

    # Obtain all of our planets.
    for planet in game_map.all_planets():
        if planet.owner == game_map.get_me().all_ships()[0].owner:
            my_planets.append(planet)

    # Check to see if it's the first turn.
    if not first_turn:
        # Go through all ships.
        for ship in game_map.get_me().all_ships():
            navigate_command = None
            # If the ship is docked, skip it.
            if ship.docking_status != ship.DockingStatus.UNDOCKED:
                continue

            # Obtain nearest junk, and sort it.  Obtained from Sentdex: https://www.youtube.com/watch?v=vC3lQ3ZJE2Y&t=223s.
            nearby_stuff = game_map.nearby_entities_by_distance(ship)
            nearby_stuff = OrderedDict(sorted(nearby_stuff.items(), key=lambda t: t[0]))
            empty_planets_by_distance = []
            for stuff in nearby_stuff:
                # For all nearby planets that are unowned.
                if isinstance(nearby_stuff[stuff][0], hlt.entity.Planet) and not nearby_stuff[stuff][0].is_owned():
                    empty_planets_by_distance.append(nearby_stuff[stuff][0])
                    # If we can dock, dock.
                    if ship.can_dock(nearby_stuff[stuff][0]):
                        navigate_command = ship.dock(nearby_stuff[stuff][0])
                        command_queue.append(navigate_command)
                    else:
                        # If we're already going to that planet, skip it.
                        if nearby_stuff[stuff][0] in planned_planets:
                            continue
                        else:
                            # Go to closest empty planet, that I'm not already planning to go to.
                            navigate_command = ship.navigate(ship.closest_point_to(nearby_stuff[stuff][0]), game_map, speed=int(hlt.constants.MAX_SPEED), ignore_ships=True)

                            # If it's possible to get there, get there.
                            if navigate_command:
                                command_queue.append(navigate_command)
                                planned_planets.append(nearby_stuff[stuff][0])
                    break  # Once we find a planet to go to or to dock on, quit looking.
            # If there's no planets to capture, start destroying planets that aren't ours.
            if len(empty_planets_by_distance) == 0 or navigate_command is None:
                for planet in game_map.all_planets():
                    if planet not in my_planets and planet not in empty_planets_by_distance:
                        navigate_command = ship.navigate(planet, game_map, speed=int(hlt.constants.MAX_SPEED), ignore_ships=True)  # Crash.
                        if navigate_command:
                            command_queue.append(navigate_command)
                        break
    else:
        # This is supposed to prevent initial crashing, but doesn't, lol.
        i = 1
        for ship in game_map.get_me().all_ships():
            navigate_command = ship.thrust(i*2, 45)
            command_queue.append(navigate_command)
            i += 1
        first_turn = False
    game.send_command_queue(command_queue) # Send list of commands.
    #  TURN END
# GAME END
