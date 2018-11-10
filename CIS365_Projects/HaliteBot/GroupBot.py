"""
Team name:
Team members: Charles Billingsley, Josh Getter, Adam Steward, Josh Techentin
"""

import hlt, logging, random, math
from collections import OrderedDict

"""
This code was built off of the starter bot given by the Halite website
"""


# HELPER FUNCTIONS
def get_distance(origin, destination):
    if origin == destination:
        return None
    else:
        return origin.calculate_distance_between(destination)


# GAME START
# Here we define the bot's name as Settler and initialize the game, including communication with the Halite engine.
game = hlt.Game("Planet KillerV2")
# Then we print our start message to the logs
logging.info("Powering up the Planet KillerV2.")

turnNum = 0
roleNum = 0
ship_targets = {}

"""
Role names:

Harvester (0) - docks on a planet to harvest for resources
Reaper (1) - attacks enemy ships
Ninja - (2) attacks docked ships to take over planets
Star Destroyer (3) - attacks planets to destroy them
Guardian (4) - circles your own planets

Roles CANNOT BE CHANGED for any reason
"""

ship_angles = {}
while True:
    # TURN START
    # Update the map for the new turn and get the latest version
    game_map = game.update_map()

    # Here we define the set of commands to be sent to the Halite engine at the end of the turn
    command_queue = []

    all_ships = game_map._all_ships()
    team_ships = game_map.get_me().all_ships()

    # Obtain all enemy ships.
    enemy_ships = []
    for player in game_map.all_players():
        if player != game_map.get_me():
            enemy_ships += player.all_ships()

    # Obtain all DOCKED enemy ships.
    docked_enemy_ships = []
    for ship in enemy_ships:
        if ship.docking_status != ship.DockingStatus.UNDOCKED:
            docked_enemy_ships.append(ship)

    all_entities = game_map.all_planets() + enemy_ships

    if turnNum == 0:  # if first turn, thrust at different speeds in same direction to avoid initial collisions
        for i in range(len(team_ships)):
            navigate_command = team_ships[i].thrust(i * 2, 45)
            command_queue.append(navigate_command)

    else:
        planets_to_dock_on = []
        enemy_planets_to_destroy = []
        my_planets = []
        for planet in game_map.all_planets():
            # If empty or our planet isn't full.
            if not planet.is_owned() or planet.owner.id == game_map.get_me().id and not planet.is_full():
                planets_to_dock_on.append(planet)
            # If I don't own the planet, and many are docked, blow it up.
            elif planet.is_owned() and planet.owner.id != game_map.get_me().id and planet.is_full() and planet.num_docking_spots > 2:
                enemy_planets_to_destroy.append(planet)
            # If it's my planet, and I don't need to dock on it, put it on our list of planets to protect.
            elif planet.is_owned() and planet.owner.id == game_map.get_me().id:
                my_planets.append(planet)

        # Find out how many ships we want to dock on planets, aka Harvesters.
        harvestersNeeded = 0
        for planet in planets_to_dock_on:
            harvestersNeeded += planet.num_docking_spots - len(planet._docked_ship_ids)

        # For every ship that I control
        for ship in team_ships:
            navigate_command = None

            # If the ship is docked
            if ship.docking_status != ship.DockingStatus.UNDOCKED:
                # Skip this ship
                continue

            # check if the ship has a role/target already
            if ship_targets.get(ship.id):
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

                # If the ship is a new ship, give it a role and target
                # Ships original target is likely gone -> Reassign a new one that is closest to it.
                # TODO assigning to target occasionally errors out (as there may be no target in list)
                if target is None:
                    if role == "Harvester":
                        planets_to_dock_on.sort(key=lambda planet: get_distance(ship, planet))
                        target = planets_to_dock_on[0]
                    elif role == "Reaper":
                        enemy_ships.sort(key=lambda enemyShip: get_distance(ship, enemyShip))
                        target = enemy_ships[0]
                    elif role == "Ninja":
                        docked_enemy_ships.sort(key=lambda enemyShip: get_distance(ship, enemyShip))
                        target = docked_enemy_ships[0]
                    elif role == "Guardian":
                        my_planets.sort(key=lambda myPlanet: get_distance(ship, myPlanet))
                        target = my_planets[0]
                    elif role == "Star Destroyer":
                        enemy_planets_to_destroy.sort(key=lambda enemyPlanet: get_distance(ship, enemyPlanet))
                        if len(enemy_planets_to_destroy) != 0:  # If there's a planet to destroy, destroy it.
                            target = enemy_planets_to_destroy[0]
                        else:  # If there's no planet to destroy, just attack a ship instead.
                            enemy_ships.sort(key=lambda enemyShip: get_distance(ship, enemyShip))
                            target = enemy_ships[0]
                    # ship_targets[ship.id][1] = target.id
                    # This may be better as a dictionary since you cant mutate tuples.
                    # For now lets create a new tuple like this:
                    ship_targets[ship.id] = (ship_targets[ship.id][0], target.id)
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
                    # add the move to the command queue and add the planet in question to planned planets if the move is possible
                    if navigate_command:
                        command_queue.append(navigate_command)
                elif role == "Reaper" or role == "Ninja":
                    # navigate toward target, taking ship collisions into effect
                    navigate_command = ship.navigate(
                        ship.closest_point_to(target),
                        game_map,
                        speed=int(hlt.constants.MAX_SPEED),
                        max_corrections=18,
                        angular_step=5,
                        ignore_ships=False)
                    # add the move to the command queue and add the planet in question to planned planets if the move is possible
                    if navigate_command:
                        command_queue.append(navigate_command)
                elif role == "Star Destroyer":
                    # navigate to center of planet, taking ship collisions into effect
                    navigate_command = ship.navigate(
                        target,
                        game_map,
                        speed=int(hlt.constants.MAX_SPEED),
                        max_corrections=18,
                        angular_step=5,
                        ignore_ships=False)
                    # add the move to the command queue and add the planet in question to planned planets if the move is possible
                    if navigate_command:
                        command_queue.append(navigate_command)
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

            else:  # this is a new ship, give it a role and a target
                # assign the ship a role
                if turnNum == 1 or roleNum == 0:
                    if harvestersNeeded > 0:
                        role = "Harvester"
                        harvestersNeeded -= 1
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
                # TODO assigning to target occasionally errors out (as there may be no target in list)
                if role == "Harvester":
                    # target defaults to closest planet in need of docking
                    planets_to_dock_on.sort(key=lambda planet: get_distance(ship, planet))
                    target = planets_to_dock_on[0]
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

    # Send our set of commands to the Halite engine for this turn
    game.send_command_queue(command_queue)
    turnNum += 1
    # TURN END
# GAME END