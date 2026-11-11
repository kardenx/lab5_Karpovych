<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <title>Реєстрація</title>
</head>
<body>
<h2>Реєстрація</h2>
<#if error??>
    <p style="color:red">${error}</p>
</#if>
<form action="/registration" method="post">
    <p><input type="text" name="username" placeholder="Логін" required></p>
    <p><input type="password" name="password" placeholder="Пароль" required></p>
    <p><button type="submit">Зареєструватися</button></p>
</form>
<p><a href="/login">Увійти</a></p>
</body>
</html>