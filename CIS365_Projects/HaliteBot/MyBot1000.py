import hlt # Import Halite Starter-kit.
import logging # Import logging module.

#GAME START
game = hlt.Game("Settler1000")
logging.info("Starting my Settler1000 bot!")

planned_planets = [] # Keep track of what planets we're traveling to.

while True:
    # TURN START  
    game_map = game.update_map() # Update the map for the new turn and get the latest version
    command_queue = [] # Commands per turn.
    
    # Go through all ships.
    for ship in game_map.get_me().all_ships():
        #If the ship is docked, skip it.
        if ship.docking_status != ship.DockingStatus.UNDOCKED:
            continue

        # Check all non-destroyed planets.
        for planet in game_map.all_planets():
            #If we own the planet, skip it.
            if planet.is_owned():
                continue

            # If we can dock, dock.
            if ship.can_dock(planet):
                command_queue.append(ship.dock(planet))
            else:
                # If we're already going to that planet, skip it.
                if planet in planned_planets:
                    continue
                else:
                    # Go to closest empty planet, that I'm not already planning to go to.
                    navigate_command = ship.navigate(ship.closest_point_to(planet), game_map, speed=int(hlt.constants.MAX_SPEED), ignore_ships=True)
                        
                    # If it's possible to get there, get there.
                    if navigate_command:
                        command_queue.append(navigate_command)
                        planned_planets.append(planet)
            break

    game.send_command_queue(command_queue) # Send list of commands.
    # TURN END
# GAME END
