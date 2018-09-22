def primeSum(n):
    some_list = [True for i in range(1000000)]
    some_list[0] = some_list[1] = False;
    prime_list = []
    prime_set = set()
    for i in range(1000000):
        if not some_list[i]:
            continue
        prime_set.add(i)
        prime_list.append(i)
        for j in range(i,1000000//i):
            some_list[j*i] = False

    # Append the output to the output list
    out = []

    # Case when n is odd
    if (n % 2 == 1):
        out.append(3)
        n -= 3

    counter = 2
    while True:
        diff = n - prime_list[counter]
        if diff in prime_set:
            out.append(diff)
            out.append(prime_list[counter])
            break
        else:
            counter += 1

    return out

print(primeSum(int(input())))
