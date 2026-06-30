<?php require_once 'config.php'; ?>
<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>08.05.a - 3 camere cel mai ieftin</title>
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div class="container">
        <a href="index.php" class="back-btn">
            <i class="fas fa-arrow-left"></i> Înapoi la meniu
        </a>
        
        <div class="table-container">
            <h2><i class="fas fa-couch"></i> 08.05.a - 3 camere cel mai ieftin</h2>
            <p>Spații cu 3 camere la cel mai mic preț de vânzare</p>
            
            <?php
            $sql = "SELECT s.id_spatiu, s.adresa, s.suprafata, o.pret, o.moneda, t.caracteristici
                    FROM Spatiu s
                    JOIN Tip t ON s.id_tip = t.id_tip
                    JOIN Oferta o ON s.id_spatiu = o.id_spatiu
                    WHERE t.caracteristici LIKE '%3 camere%'
                    AND o.vanzare = 'D'
                    AND o.pret = (
                        SELECT MIN(o2.pret)
                        FROM Oferta o2
                        JOIN Spatiu s2 ON o2.id_spatiu = s2.id_spatiu
                        JOIN Tip t2 ON s2.id_tip = t2.id_tip
                        WHERE t2.caracteristici LIKE '%3 camere%'
                        AND o2.vanzare = 'D'
                    )";
            
            try {
                $stmt = $conn->query($sql);
                $rows = $stmt->fetchAll();
                $count = count($rows);
                $minPrice = $count > 0 ? $rows[0]['pret'] : null;
            ?>
            
            <table>
                <thead>
                    <tr>
                        <th>ID Spațiu</th>
                        <th>Adresă</th>
                        <th>Suprafață</th>
                        <th>Preț</th>
                        <th>Preț/m²</th>
                    </tr>
                </thead>
                <tbody>
                    <?php foreach ($rows as $row): ?>
                    <tr>
                        <td><?php echo $row['id_spatiu']; ?></td>
                        <td><?php echo $row['adresa']; ?></td>
                        <td><?php echo $row['suprafata']; ?> m²</td>
                        <td><strong style="color: #27ae60;"><?php echo formatPrice($row['pret'], $row['moneda']); ?></strong></td>
                        <td><?php echo formatPrice($row['pret'] / $row['suprafata'], $row['moneda']); ?>/m²</td>
                    </tr>
                    <?php endforeach; ?>
                </tbody>
            </table>
            
            <div class="stats">
                <div class="stat-box">
                    <h4>Cel mai mic preț</h4>
                    <p><?php echo $minPrice !== null ? formatPrice($minPrice, 'EUR') : '-'; ?></p>
                </div>
                <div class="stat-box">
                    <h4>Număr spații</h4>
                    <p><?php echo $count; ?></p>
                </div>
            </div>
            
            <?php
            } catch(PDOException $e) {
                echo "<p style='color: red;'>Eroare SQL: " . $e->getMessage() . "</p>";
            }
            ?>
        </div>
</body>
</html>