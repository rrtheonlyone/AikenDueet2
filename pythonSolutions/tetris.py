# Pre programmed stuff

def level3(seq):
    out = []
    orot = False
    onext = 0
    inext = 9
    return [0]*100 #Return

def level2(seq):
    out = []
    lastloc = 0
    lastJpos = 0
    lastLpos = 0
    lastJrot = False
    lastLrot = False
    for i in seq:
        craft = 0
        if i == "J":
            if lastJrot:
                craft += 20
            else:
                lastJpos = lastloc
                lastloc += 2
                if lastloc > 10:
                    lastloc = 0
            lastJrot = not lastJrot
            craft += lastJpos
        # i == "L"
        else:
            if lastLrot:
                craft += 20
            else:
                lastLpos = lastloc
                lastloc += 2
                if lastloc > 10:
                    lastloc = 0
            lastLrot = not lastLrot
            craft += lastLpos
        out += craft
    return out





def general(seq):
    for i in 

def tetris(seq):
    #Level 1:
    if (seq == "O"*100):
        return [0,2,4,6,8]*20

    # Check which level
    pieces = set()
    for i in seq:
        set.add(i)

    if ("S" not in pieces):
        if ("O" not in pieces):
            return level2(seq)
        else:
            return level3(seq)
    return level4(seq)

