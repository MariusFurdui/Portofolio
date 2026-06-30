<?php require_once 'config.php'; ?>
<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>08.05.b - Agenții similare</title>
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div class="container">
        <a href="index.php" class="back-btn">
            <i class="fas fa-arrow-left"></i> Înapoi la meniu
        </a>
        
        <div class="table-container">
            <h2><i class="fas fa-users"></i> 08.05.b - Agenții similare</h2>
            <p>Agenții cu oferte identice cu agenția 1 pentru spațiul 1</p>
            
            <?php
            // interogare pentru oferta de referinta
            $refSql = "SELECT s.id_tip, o.pret, o.moneda 
                      FROM Spatiu s 
                      JOIN Oferta o ON s.id_spatiu = o.id_spatiu 
                      WHERE s.id_spatiu = 1 AND o.id_agentie = 1";
            
            try {
                $refStmt = $conn->query($refSql);
                $ref = $refStmt->fetch();
                
                if ($ref):
                    $sql = "SELECT a.nume, o.pret, o.moneda, s.adresa
                            FROM Agentie a
                            JOIN Oferta o ON a.id_agentie = o.id_agentie
                            JOIN Spatiu s ON o.id_spatiu = s.id_spatiu
                            WHERE s.id_tip = :tip_id
                            AND o.pret = :pret
                            AND o.moneda = :moneda
                            AND a.id_agentie != 1
                            ORDER BY a.nume";
                    
                    $stmt = $conn->prepare($sql);
                    $stmt->execute([
                        ':tip_id' => $ref['id_tip'],
                        ':pret' => $ref['pret'],
                        ':moneda' => $ref['moneda']
                    ]);
                    $rows = $stmt->fetchAll();
                    $count = count($rows);
            ?>
            
            <div style="background: #f8f9fa; padding: 1rem; border-radius: 5px; margin-bottom: 1rem;">
                <p><strong>Oferta de referință:</strong> Agenția 1 | Spațiul 1 | Tip ID: <?php echo $ref['id_tip']; ?> | 
                Preț: <?php echo formatPrice($ref['pret'], $ref['moneda']); ?></p>
            </div>
            
            <table>
                <thead>
                    <tr>
                        <th>Agenție</th>
                        <th>Preț</th>
                        <th>Adresă</th>
                    </tr>
                </thead>
                <tbody>
                    <?php foreach ($rows as $row): ?>
                    <tr>
                        <td><strong><?php echo $row['nume']; ?></strong></td>
                        <td><?php echo formatPrice($row['pret'], $row['moneda']); ?></td>
                        <td><?php echo $row['adresa']; ?></td>
                    </tr>
                    <?php endforeach; ?>
                </tbody>
            </table>
            
            <div class="stats">
    <?php if ($count > 0): ?>
        <div class="stat-box">
            <h4>Agenții similare</h4>
            <p><?php echo $count; ?> agenții</p>
        </div>
        
        <div class="stat-box">
            <h4>Preț comun</h4>
            <p><?php echo formatPrice($ref['pret'], $ref['moneda']); ?></p>
        </div>
        
        <div class="stat-box">
            <h4>Tip spațiu</h4>
            <p>ID <?php echo $ref['id_tip']; ?> (3 camere)</p>
        </div>
    <?php else: ?>
        <div class="stat-box" style="border-left-color: #e74c3c;">
            <h4>Agenții similare</h4>
            <p>0 găsite</p>
        </div>
    <?php endif; ?>
</div>
            
            <?php else: ?>
            <p style="color: #e74c3c; padding: 1rem; background: #fdeaea; border-radius: 5px;">
                <i class="fas fa-exclamation-circle"></i> Nu există oferta de referință (Agenția 1 - Spațiul 1)
            </p>
            <?php endif; 
            
            } catch(PDOException $e) {
                echo "<p style='color: red;'>Eroare SQL: " . $e->getMessage() . "</p>";
            }
            ?>
        </div>
    </div>
</body>
</html>