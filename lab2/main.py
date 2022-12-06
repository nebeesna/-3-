import matplotlib.pyplot as plt
import numpy as np
# Варіант 6
# Реалізувати алгоритм апроксимації  поточково  заданої  кривої кубічним сплайном
# у випадку граничних умов. Показати вигляд матриці Р’ для класичного задання кубічного сплайну.
# Вивести зміну матриці при заданні відповідних  граничних умов.
# Дослідити зміну форми кривої при модифікації граничних умов.
# Перевірити правильність форми кривої з геометричних міркувань.
# гранична умова слабка

def spline_function(a, b, c, d, x, x0):
    return a + b * (x - x0) + c * (x - x0)**2 + d * (x - x0)**3


def splines_cords(x_cords, coefficients):
    x_res = []
    y_res = []
    for i in range(len(x_cords) - 1):
        a, b, c, d = coefficients[(i * 4): ((i + 1) * 4)]
        x=[]
        step = (x_cords[i + 1] - x_cords[i])/100
        x_cord = x_cords[i]
        for j in range(100):
            x.append(x_cord)
            x_cord+=step

        y=[]
        for x_item in x:
            y.append(spline_function(a, b, c, d, x_item, x_cords[i]))

        x_res += list(x)
        y_res += list(y)
    return x_res, y_res


def calculate_coefs(x_cords, y_cords):
    if len(x_cords) == len(y_cords):
        n = (len(x_cords) - 1) * 4
        A = np.zeros(shape=[n, n])
        B = np.zeros(shape=n)

        for col in range(len(x_cords)-1):
            A[col * 2][col * 4] = 1
            B[col * 2] = y_cords[col]
            for j in range(4):
                A[col * 2 + 1][col * 4 + j] = (x_cords[col+1] - x_cords[col]) ** j
            B[col * 2 + 1] = y_cords[col + 1]

        row = (len(x_cords)-1) * 2
        for col in range(len(x_cords) - 2):
            A[row][col * 4 + 1] = 1
            A[row][col * 4 + 2] = 2 * (x_cords[col+1] - x_cords[col])
            A[row][col * 4 + 3] = 3 * (x_cords[col+1] - x_cords[col]) ** 2
            A[row][col * 4 + 5] = -1
            row += 1

            A[row][col * 4 + 2] = 2
            A[row][col * 4 + 3] = 6 * (x_cords[col+1] - x_cords[col])
            A[row][col * 4 + 6] = -2
            row += 1

        A[row][2] = 2
        A[row + 1][-2] = 2
        A[row + 1][-1] = 6 * (x_cords[-1] - x_cords[-2])

        print('A:')
        for i in range(n):
            for j in range(n):
                print(round(A[i][j], 2), end=' ')
            print()

        print('B:')
        for i in range(n):
            print(round(B[i], 2), end=' ')
        print()

        return np.linalg.solve(A, B)

    else:
        raise Exception("array size error!")


if __name__ == '__main__':
    spline, ax = plt.subplots()
    x_points = [0, 3, 10]
    y_points = [0, 7, 6]
    x, y = splines_cords(x_points, calculate_coefs(x_points, y_points))
    dots = ax.scatter(x_points, y_points, color="#000000")
    line, = ax.plot(x, y, color="#000000")

    def onclick(event):
        global x_points, y_points, dots
        x_points.append(event.xdata)
        x_points.sort()
        y_points.insert(x_points.index(event.xdata), event.ydata)

        dots.remove()
        dots = ax.scatter(x_points, y_points, color="#000000")
        line.set_data(splines_cords(x_points, calculate_coefs(x_points, y_points)))
        spline.canvas.draw()

    cid = spline.canvas.mpl_connect('button_press_event', onclick)
    ax.grid(True)
    plt.show()
