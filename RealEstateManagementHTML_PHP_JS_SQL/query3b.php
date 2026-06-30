<?php require_once 'config.php'; ?>
<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>08.03.b - Oferte vânzare EUR</title>
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div class="container">
        <a href="index.php" class="back-btn">
            <i class="fas fa-arrow-left"></i> Înapoi la meniu
        </a>
        
        <div class="table-container">
            <h2><i class="fas fa-euro-sign"></i> 08.03.b - Oferte vânzare EUR (10k-50k)</h2>
            
            <?php
            $sql = "SELECT id_agentie, id_spatiu, pret, moneda
                    FROM Oferta
                    WHERE vanzare = 'D' AND moneda = 'EUR' AND pret BETWEEN 10000 AND 50000
                    ORDER BY pret ASC";
            
            try {
                $stmt = $conn->query($sql);
                $rows = $stmt->fetchAll();
                $count = count($rows);
                $total = 0;
                
                foreach ($rows as $row) {
                    $total += $row['pret'];
                }
            ?>
            
            <table>
                <thead>
                    <tr>
                        <th>ID Agenție</th>
                        <th>ID Spațiu</th>
                        <th>Preț</th>
                        <th>Moneda</th>
                    </tr>
                </thead>
                <tbody>
                    <?php foreach ($rows as $row): ?>
                    <tr>
                        <td><?php echo $row['id_agentie']; ?></td>
                        <td><?php echo $row['id_spatiu']; ?></td>
                        <td><?php echo formatPrice($row['pret'], $row['moneda']); ?></td>
                        <td><?php echo $row['moneda']; ?></td>
                    </tr>
                    <?php endforeach; ?>
                </tbody>
            </table>
            
            <div class="stats">
                <div class="stat-box">
                    <h4>Număr oferte</h4>
                    <p><?php echo $count; ?></p>
                </div>
                <div class="stat-box">
                    <h4>Valoare totală</h4>
                    <p><?php echo formatPrice($total, 'EUR'); ?></p>
                </div>
                <div class="stat-box">
                    <h4>Preț mediu</h4>
                    <p><?php echo $count > 0 ? formatPrice($total/$count, 'EUR') : '-'; ?></p>
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