# Краткое описание алгоритма
<p>Создается здание с определенным количеством этажей и лифтов. Сначала конечно создаются этажи, затем лифт с указанием начального этажа ( у меня это первый)
Запускается поток Floor и начинают генерироваться люди на этажах. При создание пассажира указывается на какой этаж ему нужно. Люди попадают в нужную им очередь (вниз/вверх).</p>
<p>Запускается поток Elevator и проверяет есть ли у него вызовы на начальном этаже. Если вызов есть, люди заходят, пока вместимость позволяет. Дальше лифт двигается вверх, часть людей уходит, часть приходит. Лифт двигается вверх до момента, пока на верхних этажах не останется вызовов/до последнего этажа. Если необходимости двигаться выше нет (либо это последний этаж), лифт проверяет вызовы снизу и работает по аналогии с движением вверх.</p>