def primeSum(n):
    some_list = [True for i in range(100000)]
    some_list[0] = some_list[1] = False;
    prime_list = set()
    for i in range(100000):
        if not some_list[i]:
            continue
        prime_list.add(i)
        for j in range(i,100000/i):
            some_list[j*i] = False

    # Append the output to the output list
    out = []
    k = n+1

    #bookkeeping
    initn = n

    # while n is not broken
    while n:
        if k < 2:
            break
        if k in prime_list:
            out.append(k)
            n -= k
            k = n+1
        k -= 1

    if (k < 2 and n != 0):
        print initn
    else:
        print out

primeSum(int(raw_input()))
