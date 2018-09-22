theNext = {
  "boss":
    {
      "name": "joker",
      "offense": 11
    }
  ,
  "skills": [
    {
      "name": "Grapple Gun",
      "offense": 5,
      "points": 1,
      "require": "null"
    },
    {
      "name": "Hacking Device",
      "offense": 6,
      "points": 2,
      "require": "Grapple Gun"
    },
    {
      "name": "Remote",
      "offense": 7,
      "points": 3,
      "require": "Hacking Device"
    },
    {
      "name": "Bomb",
      "offense": 20,
      "points": 5,
      "require": "Remote"
    },
    {
      "name": "Inverted takedown",
      "offense": 5,
      "points": 1,
      "require": "null"
    },
    {
      "name": "Shockwave attack",
      "offense": 8,
      "points": 3,
      "require": "Inverted takedown"
    }
  ]
}



def skillTree(inputJSON):
    head = ["null", "null", "null"]
    numberSkills = [1, 1, 1]
    currcostSkills = [0, 0, 0]
    currvalueSkills = [0,0,0]
    dpCost = [[0],[0],[0]]
    dpValue = [[0],[0],[0]]
    name = [["zygotes"], ["zygotes"], ["zygotes"]]

    topcost = inputJSON["boss"]["offense"]
    skillList = inputJSON["skills"]
    while sum(numberSkills) - 3 != len(skillList):
        for i in skillList:
            if i == None:
                continue
            for j in range(3):
                if i["require"] == head[j]:
                    dpCost[j].append(currcostSkills[j] + i["points"])
                    dpValue[j].append(currvalueSkills[j] + i["offense"])
                    name[j].append(i["name"])
                    head[j] = i["name"]
                    currcostSkills[j] += i["points"]
                    currvalueSkills[j] += i["offense"]
                    numberSkills[j] += 1
                    i = None
                    break

    bestoutput = (0,0,0)
    bestcost = 9**12
    for i in range(numberSkills[0]):
        for j in range(numberSkills[1]):
            for k in range(numberSkills[2]):
                if (dpValue[0][i] + dpValue[1][j] + dpValue[2][k]) >= topcost:
                    currcost = dpCost[0][i] + dpCost[1][j] + dpCost[2][k]
                    if bestcost > currcost:
                        bestcost = currcost
                        bestoutput = (i, j, k)
                        break;

    out = []
    for i in range(len(bestoutput)):
        for j in range(1, bestoutput[i]+1):
            out.append(name[i][j])
    return (out)

skillTree(theNext)
