switch (currentWorm.profession) {
    case AGENT:
        // Strategi individu agent
        // Kalau ada yang bisa di banana bomb 2 langsung, gas aja
        // Kalau ada yang bisa dibanana bomb mati, gas aja
        // Cari target (Agent > Technologist > Commando)
        // Kalau target bisa di banana, gas aja
        // Kalau ada yang bisa ditembak, gas aja
        // Gerak ke target
        Cell targetCell = StrategyUtils.agentCanShootTwoWorms();

        if (targetCell != null) {
            return new BananaCommand(targetCell);
        }

        // target cell = ambil cell yang bisa banana musuh sampai mati (kalau ada yang ga kena teman,
        // pilih cell itu aja, kalau terpaksa kena teman, yaudah gas aja)
        if (targetCell != null) {
            return new BananaCommand(targetCell);
        }
        if (currentWorm.health < 21) {
            targetCell = StrategyUtils.desperateAgent();
            if (targetCell != null) {
                return new BananaCommand(targetCell);
            }
        }
        Worm target = StrategyUtils.setTargetWorm();

        // targetCell = getArea(target); (cari cell yang bisa nembak target dan
        // diusahakan ga kena teman)
        if (targetCell != null) {
            return new BananaCommand(targetCell);
        }
        targetCell = StrategyUtils.getAvailableShoot(currentWorm);
        if (targetCell != null) {
            Direction direction = PlaneUtils.resolveDirection(currentWorm.position,
            targetCell);
            return new ShootCommand(direction);
        }
        // Gerak ke target
        Cell nextCell = PlaneUtils.nextLine(currentWorm.position, target.position);

        if (nextCell.type == CellType.DIRT) {
            return new DigCommand(currentWorm, nextCell);
        } else {
            return new MoveCommand(currentWorm, nextCell);
        }
        break;
    case TECHNOLOGIST:
    // Strategi individu technologist
    // Kalau ada yang bisa di freeze 2 langsung, gas aja
    // Cari target (Agent > Technologist > Commando)
    // Kalau target bisa di freeze, freeze aja
    // Kalau target bisa di tembak, gas aja
    // Kalau ada yang bisa ditembak (selain target), gas aja
    // Gerak ke target
        Cell targetCell = StrategyUtils.technologistCanShootTwoWorms();

        if (targetCell != null) {
            return new SnowballCommand(targetCell);
        }

        if (currentWorm.health < 21) {
            targetCell = StrategyUtils.desperateTechnologist();
            if (targetCell != null) {
                return new SnowballCommand(targetCell);
            }
        }
        Worm target = StrategyUtils.setTargetWorm();

        // targetCell = getArea(target); (cari cell yang bisa snowball target dan
        // diusahakan ga kena teman)
        if (targetCell != null) {
            return new SnowballCommand(targetCell);
        }
        targetCell = StrategyUtils.getAvailableShoot(currentWorm);
        if (targetCell != null) {
            Direction direction = PlaneUtils.resolveDirection(currentWorm.position,
            targetCell);
            return new ShootCommand(direction);
        }
        // Gerak ke target
        Cell nextCell = PlaneUtils.nextLine(currentWorm.position, target.position);

        if (nextCell.type == CellType.DIRT) {
            return new DigCommand(currentWorm, nextCell);
        } else {
            return new MoveCommand(currentWorm, nextCell);
        }
        break;
    case COMMANDO:
    // Strategi individu commando
    // Kalau ada yang bisa ditembak, tembak aja
    // Cari target (Agent > Technologist > Commando)
    // Gerak ke target.
        Cell targetCell = StrategyUtils.getAvailableShoot(currentWorm);
        if (targetCell != null) {
            Direction direction = PlaneUtils.resolveDirection(currentWorm.position,
            targetCell);
            return new ShootCommand(direction);
        }

        Worm target = StrategyUtils.setTargetWorm();

        // Gerak ke target;
        Cell nextCell = PlaneUtils.nextLine(currentWorm.position, target.position);

        if (nextCell.type == CellType.DIRT) {
            return new DigCommand(currentWorm, nextCell);
        } else {
            return new MoveCommand(currentWorm, nextCell);
        }
        break;
}