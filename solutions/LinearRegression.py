from sklearn import linear_model

def linearRegression(inp, out, question):
    def _linearRegression(indata, outdata):
        clf = linear_model.LinearRegression()
        clf.fit([[t[0], t[1], t[2]] for t in indata], outdata)
        return clf.coef_

    def convolution(coeff, question):
        out = 0
        for i in range(3):
            out += coeff[i]*question[i]
        return out

    reg = _linearRegression(inp, out)
    return convolution(question, reg)

# inp = input
# out = output
# question = question

i = [
    [1, 2, 3],
    [2, 3, 4],
    [2, 1, 4],
    [5, 3, 2],
    [2, 1, 2]
  ]

o = [
    6,
    9,
    7,
    10,
    5
   ]
q = [3, 4, 5]

print (linearRegression(i, o, q))
