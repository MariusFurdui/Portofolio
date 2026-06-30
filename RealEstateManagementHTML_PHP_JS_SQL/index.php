<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Colocviu Final Furdui Marius</title>
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div class="header">
        <h1>Real Estate Management System</h1>
        <p>Aplicație de gestiune imobile</p>
    </div>
    <div class="container">
       

        <div class="menu">
            <a href="query3a.php" class="menu-card">
                <h3><i class="fas fa-search"></i> 08.03.a - Spații din Turda</h3>
                <p>Spații a căror adresă începe cu 'Turda' ordonate după zonă și suprafață</p>
            </a>
            
            <a href="query3b.php" class="menu-card">
                <h3><i class="fas fa-euro-sign"S></i> 08.03.b - Oferte vânzare EUR</h3>
                <p>Oferte de vânzare cu prețul între 10,000 și 50,000 EUR</p>
            </a>

            <a href="query4a.php" class="menu-card">
                <h3><i class="fas fa-key"></i> 08.04.a - Apartamente de închiriat</h3>
                <p>Apartamente de închiriat cu preț între 100 și 400 EUR</p>
            </a>
            
            <a href="query4b.php" class="menu-card">
                <h3><i class="fas fa-balance-scale"></i> 08.04.b - Comparație prețuri</h3>
                <p>Perechi de spații cu diferența de preț sub 100 EUR</p>
            </a>

            <a href="query5a.php" class="menu-card">
                <h3><i class="fas fa-couch"></i> 08.05.a - 3 camere cel mai ieftin</h3>
                <p>Spații cu 3 camere la cel mai mic preț de vânzare</p>
            </a>
            
            <a href="query5b.php" class="menu-card">
                <h3><i class="fas fa-users"></i> 08.05.b - Agenții similare</h3>
                <p>Agenții cu oferte identice cu agenția 1 pentru spațiul 1</p>
            </a>

            <a href="query6a.php" class="menu-card">
                <h3><i class="fas fa-chart-bar"></i> 08.06.a - Statistici garsoniere</h3>
                <p>Statistici prețuri garsoniere grupate pe monedă</p>
            </a>
            
            <a href="query6b.php" class="menu-card">
                <h3><i class="fas fa-car"></i> 08.06.b - Statistici garaje</h3>
                <p>Statistici prețuri garaje grupate pe zonă</p>
            </a>

            <!-- procedura stocata -->
            <a href="procedure.php" class="menu-card" style="border-left-color: #f39c12;">
                <h3><i class="fas fa-stethoscope"></i> Consultă oferte</h3>
                <p>Căutare oferte dupa tipul de proprietate</p>
            </a>
        </div>

        <footer class="footer">
            <p>Sistem realizat pentru colocviul final de laborator BD</p>
            <p>SQL & PHP</p>
            <p>Furdui Marius - Grupa 30226</p>
        </footer>
    </div>
</body>
</html>