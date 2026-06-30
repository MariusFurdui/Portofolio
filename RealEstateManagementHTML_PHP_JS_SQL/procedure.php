<?php require_once 'config.php'; ?>
<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Procedură Stocată</title>
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
    <div class="container">
        <a href="index.php" class="back-btn">
            <i class="fas fa-arrow-left"></i> Înapoi la meniu
        </a>
        
        <div class="form-container">
            <h2><i class="fas fa-stethoscope"></i> Procedură Stocată</h2>
            <p>Caută oferte după tipul proprietății:</p>
            
            <form method="post">
                <div class="form-group">
                    <label for="tip">Tip proprietate:</label>
                    <select name="tip" id="tip" required>
                        <option value="">-- Alege --</option>
                        <option value="apartament">Apartament</option>
                        <option value="garsoniera">Garsonieră</option>
                        <option value="casa">Casă</option>
                        <option value="garaj">Garaj</option>
                        <option value="birou">Birou</option>
                    </select>
                </div>
                <button type="submit" class="submit-btn">
                    <i class="fas fa-search"></i> Caută
                </button>
            </form>
        </div>
        
        <?php 
        if ($_SERVER['REQUEST_METHOD'] == 'POST' && !empty($_POST['tip'])): 
            $tip = $_POST['tip'];
        ?>
        
        <div class="table-container">
            <h3><i class="fas fa-list"></i> Rezultate pentru: <?php echo htmlspecialchars($tip); ?></h3>
            
            <?php
            try {
                // verifica daca procedura exista
                $checkProc = "SHOW PROCEDURE STATUS WHERE Db = DATABASE() AND Name = 'GetOferteByTipSpatiu'";
                $procExists = $conn->query($checkProc)->rowCount() > 0;
                
                if ($procExists) {
                    // foloseste procedura stocata
                    $sql = "CALL GetOferteByTipSpatiu(:tip)";
                    $source = "procedură stocată";
                } else {
                    // foloseste interogare directa
                    $sql = "SELECT s.id_spatiu, s.adresa, s.zona, s.suprafata, 
                                   o.pret, o.moneda, o.vanzare,
                                   CASE o.vanzare 
                                       WHEN 'D' THEN 'Vânzare' 
                                       ELSE 'Închiriere' 
                                   END as tip_oferta
                            FROM Spatiu s
                            JOIN Tip t ON s.id_tip = t.id_tip
                            JOIN Oferta o ON s.id_spatiu = o.id_spatiu
                            WHERE t.denumire = :tip
                            ORDER BY o.pret";
                    $source = "interogare directă";
                }
                
                $stmt = $conn->prepare($sql);
                $stmt->bindParam(':tip', $tip, PDO::PARAM_STR);
                $stmt->execute();
                $rows = $stmt->fetchAll();
                $count = count($rows);
                
                echo "<p><small>Rezultate obținute prin: <strong>$source</strong></small></p>";
                
            } catch(PDOException $e) {
                echo "<div style='background: #fdeaea; color: #e74c3c; padding: 1rem; border-radius: 5px; margin: 1rem 0;'>
                        <i class='fas fa-exclamation-triangle'></i> Eroare: " . 
                        htmlspecialchars($e->getMessage()) . "
                      </div>";
                $rows = [];
                $count = 0;
            }
            ?>
            
            <?php if ($count > 0): ?>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Adresă</th>
                        <th>Zonă</th>
                        <th>Suprafață</th>
                        <th>Tip</th>
                        <th>Preț</th>
                    </tr>
                </thead>
                <tbody>
                    <?php foreach ($rows as $row): 
                        // normalizez numele coloanelor (pentru Oracle vs MySQL)
                        $zona = $row['zona'] ?? $row['ZONA'] ?? 'N/A';
                        $adresa = $row['adresa'] ?? $row['ADRESA'] ?? '';
                        $suprafata = $row['suprafata'] ?? $row['SUPRAFATA'] ?? 0;
                        $pret = $row['pret'] ?? $row['PRET'] ?? 0;
                        $moneda = $row['moneda'] ?? $row['MONEDA'] ?? 'EUR';
                        $tip_oferta = $row['tip_oferta'] ?? getOfferType($row['vanzare'] ?? 'D');
                    ?>
                    <tr>
                        <td><?php echo $row['id_spatiu'] ?? $row['ID_SPATIU'] ?? ''; ?></td>
                        <td><?php echo htmlspecialchars($adresa); ?></td>
                        <td><?php echo htmlspecialchars($zona); ?></td>
                        <td><?php echo number_format($suprafata, 0, ',', '.'); ?> m²</td>
                        <td><?php echo htmlspecialchars($tip_oferta); ?></td>
                        <td><?php echo formatPrice($pret, $moneda); ?></td>
                    </tr>
                    <?php endforeach; ?>
                </tbody>
            </table>
            
            <div class="stats">
                <div class="stat-box">
                    <h4>Oferte găsite</h4>
                    <p><?php echo $count; ?></p>
                </div>
                <div class="stat-box">
                    <h4>Tip căutat</h4>
                    <p><?php echo htmlspecialchars($tip); ?></p>
                </div>
            </div>
            
            <?php else: ?>
            <div style="background: #f8f9fa; padding: 2rem; text-align: center; border-radius: 5px; margin-top: 1rem;">
                <i class="fas fa-search" style="font-size: 3rem; color: #95a5a6;"></i>
                <h3>Nu s-au găsit oferte</h3>
                <p>Încearcă cu un alt tip de proprietate.</p>
            </div>
            <?php endif; ?>
        </div>
        <?php endif; ?>
    </div>
</body>
</html>