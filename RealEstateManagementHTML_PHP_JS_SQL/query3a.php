<?php require_once 'config.php'; ?>
<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>08.03.a - Spații din Turda</title>
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div class="container">
        <a href="index.php" class="back-btn">
            <i class="fas fa-arrow-left"></i> Înapoi la meniu
        </a>
        
        <div class="table-container">
            <h2><i class="fas fa-map-marker-alt"></i> 08.03.a - Spații din Turda</h2>
            <p>Rezultate ordonate după zonă și suprafață</p>
            
            <?php
            $sql = "SELECT id_spatiu, adresa, zona, suprafata
                    FROM Spatiu
                    WHERE adresa LIKE 'Turda%'
                    ORDER BY zona ASC, suprafata DESC";
            
            try {
                $stmt = $conn->query($sql);
                $rows = $stmt->fetchAll();
                $count = count($rows);
            ?>
            
            <table>
                <thead>
                    <tr>
                        <th>ID Spațiu</th>
                        <th>Adresă</th>
                        <th>Zonă</th>
                        <th>Suprafață (m²)</th>
                    </tr>
                </thead>
                <tbody>
                    <?php foreach ($rows as $row): ?>
                    <tr>
                        <td><?php echo $row['id_spatiu']; ?></td>
                        <td><?php echo $row['adresa']; ?></td>
                        <td><?php echo $row['zona']; ?></td>
                        <td><?php echo number_format($row['suprafata'], 0, ',', '.'); ?></td>
                    </tr>
                    <?php endforeach; ?>
                </tbody>
            </table>
            
            <div class="stats">
                <div class="stat-box">
                    <h4>Total spații</h4>
                    <p><?php echo $count; ?></p>
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