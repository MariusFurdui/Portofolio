<?php require_once 'config.php'; ?>
<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>08.04.b - Comparație prețuri</title>
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div class="container">
        <a href="index.php" class="back-btn">
            <i class="fas fa-arrow-left"></i> Înapoi la meniu
        </a>
        
        <div class="table-container">
            <h2><i class="fas fa-balance-scale"></i> 08.04.b - Comparație prețuri</h2>
            <p>Perechi de spații cu diferența de preț sub 100 EUR</p>
            
            <?php
            $sql = "SELECT DISTINCT o1.id_spatiu AS id_spatiu1, o2.id_spatiu AS id_spatiu2, 
                           o1.pret AS pret1, o2.pret AS pret2,
                           ABS(o1.pret - o2.pret) AS diferenta_pret,
                           o1.id_agentie
                    FROM Oferta o1
                    JOIN Oferta o2 ON o1.id_agentie = o2.id_agentie
                    WHERE o1.vanzare = 'D' AND o2.vanzare = 'D'
                    AND o1.id_spatiu < o2.id_spatiu
                    AND ABS(o1.pret - o2.pret) < 100
                    ORDER BY diferenta_pret";
            
            try {
                $stmt = $conn->query($sql);
                $rows = $stmt->fetchAll();
                $count = count($rows);
            ?>
            
            <table>
                <thead>
                    <tr>
                        <th>Agenție ID</th>
                        <th>Spațiu 1 ID</th>
                        <th>Spațiu 2 ID</th>
                        <th>Preț 1</th>
                        <th>Preț 2</th>
                        <th>Diferență</th>
                    </tr>
                </thead>
                <tbody>
                    <?php foreach ($rows as $row): ?>
                    <tr>
                        <td><?php echo $row['id_agentie']; ?></td>
                        <td><?php echo $row['id_spatiu1']; ?></td>
                        <td><?php echo $row['id_spatiu2']; ?></td>
                        <td><?php echo formatPrice($row['pret1'], 'EUR'); ?></td>
                        <td><?php echo formatPrice($row['pret2'], 'EUR'); ?></td>
                        <td><strong><?php echo formatPrice($row['diferenta_pret'], 'EUR'); ?></strong></td>
                    </tr>
                    <?php endforeach; ?>
                </tbody>
            </table>
            
            <div class="stats">
                <div class="stat-box">
                    <h4>Perechi similare</h4>
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