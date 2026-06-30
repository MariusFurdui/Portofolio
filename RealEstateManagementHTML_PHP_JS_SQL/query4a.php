<?php require_once 'config.php'; ?>
<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>08.04.a - Apartamente de închiriat</title>
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div class="container">
        <a href="index.php" class="back-btn">
            <i class="fas fa-arrow-left"></i> Înapoi la meniu
        </a>
        
        <div class="table-container">
            <h2><i class="fas fa-key"></i> 08.04.a - Apartamente de închiriat (100-400 EUR)</h2>
            
            <?php
            $sql = "SELECT s.adresa, s.zona, s.suprafata, t.caracteristici, o.pret, o.moneda
                    FROM Spatiu s
                    JOIN Tip t ON s.id_tip = t.id_tip
                    JOIN Oferta o ON s.id_spatiu = o.id_spatiu
                    WHERE o.vanzare = 'N'
                    AND t.denumire = 'apartament'
                    AND o.moneda = 'EUR'
                    AND o.pret BETWEEN 100 AND 400";
            
            try {
                $stmt = $conn->query($sql);
                $rows = $stmt->fetchAll();
                $count = count($rows);
                $totalRent = 0;
                
                foreach ($rows as $row) {
                    $totalRent += $row['pret'];
                }
            ?>
            
            <table>
                <thead>
                    <tr>
                        <th>Adresă</th>
                        <th>Zonă</th>
                        <th>Suprafață</th>
                        <th>Caracteristici</th>
                        <th>Preț/lună</th>
                    </tr>
                </thead>
                <tbody>
                    <?php foreach ($rows as $row): ?>
                    <tr>
                        <td><?php echo $row['adresa']; ?></td>
                        <td><?php echo $row['zona']; ?></td>
                        <td><?php echo $row['suprafata']; ?> m²</td>
                        <td><?php echo substr($row['caracteristici'], 0, 50) . '...'; ?></td>
                        <td><?php echo formatPrice($row['pret'], $row['moneda']); ?>/lună</td>
                    </tr>
                    <?php endforeach; ?>
                </tbody>
            </table>
            
            <div class="stats">
                <div class="stat-box">
                    <h4>Număr apartamente</h4>
                    <p><?php echo $count; ?></p>
                </div>
                <div class="stat-box">
                    <h4>Chirie medie</h4>
                    <p><?php echo $count > 0 ? formatPrice($totalRent/$count, 'EUR') : '-'; ?>/lună</p>
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