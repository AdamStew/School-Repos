import hlt, logging, random, math
from collections import OrderedDict

# HELPER FUNCTIONS
def get_distance(origin, destination):
    if origin == destination:
        return None
    else:
        return origin.calculate_distance_between(destination)


# GAME START
game = hlt.Game("Planet KillerV3")
logging.info("Starting my Planet KillerV3 bot!")


turnNum = 0
roleNum = 0
ship_targets = {}

while True:
    # TURN START  
    game_map = game.update_map()  # Update the map for the new turn and get the latest version
    command_queue = []  # Keep track of commands.
    my_planets = []  # Keep track of what planets I own.
    enemy_ships = []  # Keep track of all enemy ships.
    docked_enemy_ships = []  # Keep track of all enemy docked ships.
    planets_to_harvest = []  # Keep track of planets I want to harvest from.
    enemy_planets_to_destroy = []  # Keep track of planets to destroy.

    # Obtain all enemy ships.
    for player in game_map.all_players():
        if player != game_map.get_me():
            enemy_ships += player.all_ships()

    # Obtain all DOCKED enemy ships.
    for ship in enemy_ships:
        if ship.docking_status != ship.DockingStatus.UNDOCKED:
            docked_enemy_ships.append(ship)

    # Obtain all of our planets.
    for planet in game_map.all_planets():
        # If it's my planet, append.
        if planet.owner == game_map.get_me().all_ships()[0].owner:
            my_planets.append(planet)
        # If I don't own the planet, and many are docked, blow it up.
        elif planet.is_owned() and planet.owner.id != game_map.get_me().id and planet.is_full() and planet.num_docking_spots > 2:
            enemy_planets_to_destroy.append(planet)
        # If no one owns it, append.
        elif not planet.is_owned():
            planets_to_harvest.append(planet)

    # Check to see if it's the first turn.
    if turnNum != 0:
        # Go through all ships.
        for ship in game_map.get_me().all_ships():
            navigate_command = None
            # If the ship is docked, skip it.
            if ship.docking_status != ship.DockingStatus.UNDOCKED:
                continue

            # If we don't have a role.
            if not (ship_targets.get(ship.id)):
                # assign the ship a role
                if turnNum == 1 or roleNum == 0:
                    if len(planets_to_harvest) > 0:
                        role = "Harvester"
                    else:
                        roleNum += 1  # If we don't need harvesters, just become a reaper.
                elif roleNum == 1:
                    if len(enemy_ships) > 0:
                        role = "Reaper"
                    else:
                        roleNum += 1  # If we don't need reapers, just become a ninja.
                elif roleNum == 2:
                    if len(docked_enemy_ships) > 0:
                        role = "Ninja"
                    else:
                        roleNum += 1  # If we don't need ninjas, just become a star destroyer.
                elif roleNum == 3:
                    if len(enemy_planets_to_destroy) > 0:
                        role = "Star Destroyer"
                    else:
                        roleNum += 1  # If we don't need a star destroyer, just become a guardian.
                elif roleNum == 4:
                    role = "Guardian"

                roleNum = (roleNum + 1) % 5  # Increment next role number.
                # If we're getting to mid-late game, make the chances of creating a harvest lower.
                if roleNum == 0 and turnNum > 100 and random.randint(0, 3) > 0:
                    roleNum += 1

                # assign the ship a target
                target = None
                if role == "Harvester":
                    # target defaults to closest planet in need of docking
                    planets_to_harvest.sort(key=lambda planet: get_distance(ship, planet))
                    target = planets_to_harvest[0]
                elif role == "Reaper":
                    # target defaults to closest enemy ship
                    enemy_ships.sort(key=lambda enemyShip: get_distance(ship, enemyShip))
                    target = enemy_ships[0]
                elif role == "Guardian":
                    # target is planet it spawned on (closest planet)
                    my_planets.sort(key=lambda myPlanet: get_distance(ship, myPlanet))
                    target = my_planets[0]
                elif role == "Ninja":
                    # target defaults to closest ship docked on an enemy planet
                    docked_enemy_ships.sort(key=lambda enemyShip: get_distance(ship, enemyShip))
                    target = docked_enemy_ships[0]
                elif role == "Star Destroyer":
                    # target defaults to closest enemy planet in need of destruction
                    if enemy_planets_to_destroy:
                        enemy_planets_to_destroy.sort(key=lambda enemyPlanet: get_distance(ship, enemyPlanet))
                        target = enemy_planets_to_destroy[0]

                ship_targets[ship.id] = (role, target.id)

            else:
                role = ship_targets[ship.id][0]
                target_id = ship_targets[ship.id][1]

                target = None
                # Check if ship already has an assigned planet
                target = game_map.get_planet(target_id)
                # If it doesn't have a planet to destroy, check if ship already has assigned ship
                if target is None:
                    for player in game_map.all_players():
                        if player != game_map.get_me():
                            target = player.get_ship(target_id)
                        if target is not None:
                            break

                # If we're done with our mission, just receive a new role.
                if target is None:
                    ship_targets.pop(ship.id)
                    continue
                else:
                    if role == "Harvester":
                        # check if the ship can dock at the planet
                        if ship.can_dock(target):
                            navigate_command = ship.dock(target)
                        else:
                            # navigate toward target, taking ship collisions into effect
                            navigate_command = ship.navigate(
                                ship.closest_point_to(target),
                                game_map,
                                speed=int(hlt.constants.MAX_SPEED),
                                max_corrections=18,
                                angular_step=5,
                                ignore_ships=False)
                    elif role == "Reaper" or role == "Ninja":
                        # navigate toward target, taking ship collisions into effect
                        navigate_command = ship.navigate(
                            ship.closest_point_to(target),
                            game_map,
                            speed=int(hlt.constants.MAX_SPEED),
                            max_corrections=18,
                            angular_step=5,
                            ignore_ships=False)
                    elif role == "Star Destroyer":
                        # navigate to center of planet, taking ship collisions into effect
                        navigate_command = ship.navigate(
                            target,
                            game_map,
                            speed=int(hlt.constants.MAX_SPEED),
                            max_corrections=18,
                            angular_step=5,
                            ignore_ships=False)
                    elif role == "Guardian":
                        # The variable 'target' will be the planet it circles
                        # Guards should circle the planet
                        position = target
                        # Calculate next point in circle to move to using sin and cos
                        angle = ship_angles[ship.id]['angle'] + (math.pi / 8)
                        ship_angles[ship.id]['angle'] = angle
                        position.x = (planet.radius + 3) * math.cos(angle) + planet.x
                        position.y = (planet.radius + 3) * math.sin(angle) + planet.y
                        navigate_command = ship.navigate(
                            position,
                            game_map,
                            speed=int(hlt.constants.MAX_SPEED),
                            max_corrections=18,
                            angular_step=5,
                            ignore_ships=False)

                    # add the move to the command queue
                    if navigate_command:
                        command_queue.append(navigate_command)

                    # the ship has acted, proceed to the next ship
                    continue

    else:
        i = 1
        for ship in game_map.get_me().all_ships():
            navigate_command = ship.thrust(i * 2, 45)
            command_queue.append(navigate_command)
            i += 1

    game.send_command_queue(command_queue)  # Send list of commands.
    turnNum += 1
    #  TURN END
# GAME END
