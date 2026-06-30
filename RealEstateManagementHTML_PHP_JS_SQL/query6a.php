<?php require_once 'config.php'; ?>
<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>08.06.a - Statistici garsoniere</title>
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div class="container">
        <a href="index.php" class="back-btn">
            <i class="fas fa-arrow-left"></i> Înapoi la meniu
        </a>
        
        <div class="form-container">
            <h2><i class="fas fa-chart-bar"></i> 08.06.a - Statistici garsoniere</h2>
            <p>Statistici prețuri garsoniere grupate pe monedă</p>
            
            <form method="GET" action="">
                <div class="form-group">
                    <label for="moneda"><i class="fas fa-filter"></i> Filtrează după monedă:</label>
                    <select name="moneda" id="moneda" class="form-select">
                        <option value="">Toate monedele</option>
                        <option value="EUR" <?php echo (isset($_GET['moneda']) && $_GET['moneda'] == 'EUR') ? 'selected' : ''; ?>>Euro (EUR)</option>
                        <option value="RON" <?php echo (isset($_GET['moneda']) && $_GET['moneda'] == 'RON') ? 'selected' : ''; ?>>Leu (RON)</option>
                        <option value="USD" <?php echo (isset($_GET['moneda']) && $_GET['moneda'] == 'USD') ? 'selected' : ''; ?>>Dolar (USD)</option>
                    </select>
                </div>
                <button type="submit" class="submit-btn">
                    <i class="fas fa-filter"></i> Aplică filtru
                </button>
                <a href="query6a.php" class="back-btn" style="background: #95a5a6;">
                    <i class="fas fa-times"></i> Resetează
                </a>
            </form>
        </div>
        
        <div class="table-container">
            <h3>
                <i class="fas fa-chart-line"></i> Rezultate
                <?php if (isset($_GET['moneda']) && !empty($_GET['moneda'])): ?>
                pentru moneda: <span style="color: #e74c3c;"><?php echo htmlspecialchars($_GET['moneda']); ?></span>
                <?php else: ?>
                pentru toate monedele
                <?php endif; ?>
            </h3>
            
            <?php
            // interogarea in fct de filtre
            $monedaFilter = isset($_GET['moneda']) && !empty($_GET['moneda']) ? $_GET['moneda'] : null;
            
            $sql = "SELECT 
                        moneda,
                        MIN(pret) AS pret_minim,
                        ROUND(AVG(pret), 2) AS pret_mediu,
                        MAX(pret) AS pret_maxim,
                        COUNT(*) as numar_oferte
                    FROM Oferta o
                    JOIN Spatiu s ON o.id_spatiu = s.id_spatiu
                    JOIN Tip t ON s.id_tip = t.id_tip
                    WHERE t.denumire = 'garsoniera'
                    AND o.vanzare = 'D'";
            
            // adaug filtrul pentru moneda
            if ($monedaFilter) {
                $sql .= " AND o.moneda = :moneda";
            }
            
            $sql .= " GROUP BY moneda ORDER BY moneda";
            
            try {
                $stmt = $conn->prepare($sql);
                
                if ($monedaFilter) {
                    $stmt->execute([':moneda' => $monedaFilter]);
                } else {
                    $stmt->execute();
                }
                
                $rows = $stmt->fetchAll();
                $count = count($rows);
            ?>
            
            <?php if ($count > 0): ?>
                <table>
                    <thead>
                        <tr>
                            <th>Moneda</th>
                            <th>Număr oferte</th>
                            <th>Preț minim</th>
                            <th>Preț mediu</th>
                            <th>Preț maxim</th>
                        </tr>
                    </thead>
                    <tbody>
                        <?php foreach ($rows as $row): ?>
                        <tr>
                            <td><strong><?php echo $row['moneda']; ?></strong></td>
                            <td>
                                <span class="badge" style="background: #3498db; color: white; padding: 0.25rem 0.75rem; border-radius: 50px;">
                                    <?php echo $row['numar_oferte']; ?>
                                </span>
                            </td>
                            <td><?php echo formatPrice($row['pret_minim'], $row['moneda']); ?></td>
                            <td><?php echo formatPrice($row['pret_mediu'], $row['moneda']); ?></td>
                            <td><?php echo formatPrice($row['pret_maxim'], $row['moneda']); ?></td>
                        </tr>
                        <?php endforeach; ?>
                    </tbody>
                </table>
                
                <div class="stats">
                    <div class="stat-box">
                        <h4>Monede diferite</h4>
                        <p><?php echo $count; ?></p>
                    </div>
                    
                    <?php if ($count == 1): ?>
                        <?php $row = $rows[0]; ?>
                        <div class="stat-box">
                            <h4>Total oferte</h4>
                            <p><?php echo $row['numar_oferte']; ?></p>
                        </div>
                        
                        <div class="stat-box">
                            <h4>Interval preț</h4>
                            <p>
                                <?php echo formatPrice($row['pret_minim'], $row['moneda']); ?> 
                                - 
                                <?php echo formatPrice($row['pret_maxim'], $row['moneda']); ?>
                            </p>
                        </div>
                        
                        <div class="stat-box">
                            <h4>Preț mediu</h4>
                            <p><?php echo formatPrice($row['pret_mediu'], $row['moneda']); ?></p>
                        </div>
                    <?php endif; ?>
                </div>
                
            <?php else: ?>
                <div style="text-align: center; padding: 2rem; background: #f8f9fa; border-radius: 5px;">
                    <i class="fas fa-chart-bar" style="font-size: 3rem; color: #bdc3c7; margin-bottom: 1rem;"></i>
                    <h3 style="color: #7f8c8d;">Nu există date</h3>
                    <p style="color: #95a5a6;">
                        Nu s-au găsit statistici pentru garsoniere 
                        <?php if ($monedaFilter): ?>
                        în moneda <?php echo htmlspecialchars($monedaFilter); ?>
                        <?php endif; ?>
                    </p>
                </div>
            <?php endif; ?>
            
            <?php
            } catch(PDOException $e) {
                echo "<div style='background: #fdeaea; padding: 1rem; border-radius: 5px; border-left: 4px solid #e74c3c;'>
                        <i class='fas fa-exclamation-triangle' style='color: #e74c3c;'></i>
                        <span style='color: #c0392b; margin-left: 0.5rem;'>Eroare SQL: " . $e->getMessage() . "</span>
                      </div>";
            }
            ?>
        </div>
    </div>
</body>
</html>