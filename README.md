# Arcesium

You are given a <code>List</code> of <code>List</code> of <code>String</code>.
The inner List is of the format :
"PlayerName" "Height" "BMI" "Goals Scored" "Goals Defended"

To make it to the team, the player must satisfy the following fitness criteria :
Height > 6.1
BMI < 23

To make it to the team, the player must satisfy the following experience criteria :
GoalsScored > 50 ( This would make him a STRIKER )
GoalsDefended < 30 ( This would make him a DEFENDER )

The output should be a sorted (by name) list of of list of players
The output should consist of equal number of players from each category - STRIKER or DEFENDER
The output list should be of the following format
"NAME" "SELECT/REJECT" "POSITION"

In case of clash the player with the better experience criteria should be selected