<?php require_once 'config.php'; ?>
<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>08.06.b - Statistici garaje</title>
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div class="container">
        <a href="index.php" class="back-btn">
            <i class="fas fa-arrow-left"></i> Înapoi la meniu
        </a>
        
        <div class="table-container">
            <h2><i class="fas fa-car"></i> 08.06.b - Statistici garaje</h2>
            <p>Statistici prețuri garaje grupate pe zonă</p>
            
            <?php
            $sql = "SELECT 
                        s.zona,
                        MIN(pret) AS pret_minim,
                        ROUND(AVG(pret), 2) AS pret_mediu,
                        MAX(pret) AS pret_maxim,
                        COUNT(*) as numar_garaje
                    FROM Oferta o
                    JOIN Spatiu s ON o.id_spatiu = s.id_spatiu
                    JOIN Tip t ON s.id_tip = t.id_tip
                    WHERE t.denumire = 'garaj'
                    AND o.vanzare = 'N'
                    GROUP BY s.zona
                    ORDER BY s.zona";
            
            try {
                $stmt = $conn->query($sql);
                $rows = $stmt->fetchAll();
                $count = count($rows);
                $totalGarages = 0;
                
                foreach ($rows as $row) {
                    $totalGarages += $row['numar_garaje'];
                }
            ?>
            
            <table>
                <thead>
                    <tr>
                        <th>Zonă</th>
                        <th>Număr garaje</th>
                        <th>Preț minim/lună</th>
                        <th>Preț mediu/lună</th>
                        <th>Preț maxim/lună</th>
                    </tr>
                </thead>
                <tbody>
                    <?php foreach ($rows as $row): ?>
                    <tr>
                        <td><strong>Zona <?php echo $row['zona']; ?></strong></td>
                        <td><?php echo $row['numar_garaje']; ?></td>
                        <td><?php echo formatPrice($row['pret_minim'], 'EUR'); ?>/lună</td>
                        <td><?php echo formatPrice($row['pret_mediu'], 'EUR'); ?>/lună</td>
                        <td><?php echo formatPrice($row['pret_maxim'], 'EUR'); ?>/lună</td>
                    </tr>
                    <?php endforeach; ?>
                </tbody>
            </table>
            
            <div class="stats">
                <div class="stat-box">
                    <h4>Zone diferite</h4>
                    <p><?php echo $count; ?></p>
                </div>
                <div class="stat-box">
                    <h4>Total garaje</h4>
                    <p><?php echo $totalGarages; ?></p>
                </div>
            </div>
            
            <?php
            } catch(PDOException $e) {
                echo "<p style='color: red;'>Eroare SQL: " . $e->getMessage() . "</p>";
            }
            ?>
        </div>
    </div>
</body>
</html>