# Minestom 2026.07.22-26.2 - All Metadata Support by JAB
## Common metadata layers

- `EntityMeta`, available to every entity: `airTicks`, `customName`, `customNameVisible`, `flyingWithElytra`, `hasGlowingEffect`, `hasNoGravity`, `invisible`, `onFire`, `pose`, `silent`, `sneaking`, `sprinting`, `swimming`, `tickFrozen`
- `LivingEntityMeta`: `activeHand`, `arrowCount`, `bedInWhichSleepingPosition`, `beeStingerCount`, `effectParticles`, `handActive`, `health`, `inRiptideSpinAttack`, `potionEffectAmbient`
- `MobMeta`: `aggressive`, `leftHanded`, `noAi`
- `AgeableMobMeta`: `ageLocked`, `baby`
- `TameableAnimalMeta`: `owner`, `sitting`, `tamed`
- `AbstractFishMeta`: `fromBucket`
- `AbstractHorseMeta`: `eating`, `hasBred`, `mouthOpen`, `rearing`, `tamed`
- `ChestedHorseMeta`: `hasChest`
- `BasePiglinMeta`: `immuneToZombification`
- `RaiderMeta`: `celebrating`
- `AbstractVillagerMeta`: `headShakeTimer`
- `AbstractCubeMeta`: `size`
- `AbstractArrowMeta`: `critical`, `inGround`, `noClip`, `piercingLevel`
- `AbstractVehicleMeta`: `shakingDirection`, `shakingMultiplier`, `shakingTicks`
- `AbstractMinecartMeta`: `customBlockState`, `customBlockYPosition`
- `AbstractDisplayMeta`: `billboardRenderConstraints`, `brightness`, `brightnessOverride`, `glowColorOverride`, `height`, `leftRotation`, `posRotInterpolationDuration`, `rightRotation`, `scale`, `shadowRadius`, `shadowStrength`, `transformationInterpolationDuration`, `transformationInterpolationStartDelta`, `translation`, `viewRange`, `width`
- `HangingMeta`: `direction`
- `ThrownItemProjectileMeta`: `item`
- `AbstractNautilusMeta`: `dashing`
- `AbstractWindChargeMeta`: `shooter`

## Entity-specific additions

### A–C

- Allay: `canDuplicate`, `dancing`
- Area-effect cloud: `particle`, `radius`, `waiting`
- Armadillo: `state`
- Armor stand: `bodyRotation`, `hasArms`, `hasNoBasePlate`, `headRotation`, `leftArmRotation`, `leftLegRotation`, `marker`, `rightArmRotation`, `rightLegRotation`, `small`
- Arrow: `color`, `shooter`
- Avatar: `capeEnabled`, `displayedSkinParts`, `hatEnabled`, `jacketEnabled`, `leftLegEnabled`, `leftSleeveEnabled`, `mainHand`, `rightLegEnabled`, `rightSleeveEnabled`
- Axolotl: `fromBucket`, `playingDead`, `variant`
- Bat: `hanging`
- Bee: `angerEndTime`, `hasNectar`, `hasStung`, `rolling`
- Blaze: `onFire`
- Block display: `blockState`
- Boat: `leftPaddleTurning`, `rightPaddleTurning`, `splashTimer`
- Bogged: `sheared`
- Camel: `dashing`, `lastPoseChangeTick`
- Cat: `collarColor`, `lying`, `relaxed`, `variant`
- Chicken: `variant`
- Command-block minecart: `command`, `lastOutput`
- Copper golem: `state`, `weatherState`
- Cow: `variant`
- Creaking: `active`, `canMove`, `homePos`, `tearingDown`
- Creeper: `charged`, `ignited`, `state`

### D–H

- Dolphin: `hasFish`, `moistureLevel`
- Dragon fireball: `shooter`
- End crystal: `beamTarget`, `showingBottom`
- Ender dragon: `phase`
- Enderman: `carriedBlock`, `screaming`, `staring`
- Experience orb: `value`
- Eye of Ender: `item`
- Falling block: `block`, `spawnPosition`
- Fireball: `item`, `shooter`
- Firework rocket: `fireworkInfo`, `shooter`, `shooterEntityId`, `shotAtAngle`
- Fishing hook: `catchable`, `hookedEntity`, `hookedEntityId`, `ownerEntity`
- Fox: `defending`, `faceplanted`, `firstUUID`, `foxSneaking`, `interested`, `pouncing`, `secondUUID`, `sitting`, `sleeping`, `variant`
- Frog: `tongueTarget`, `variant`
- Furnace minecart: `hasFuel`
- Ghast: `attacking`
- Glow squid: `darkTicksRemaining`
- Goat: `leftHorn`, `rightHorn`, `screaming`
- Guardian: `retractingSpikes`, `target`, `targetEntityId`
- Happy ghast: `leashHolder`, `staysStill`
- Hoglin: `immuneToZombification`
- Horse: `marking`, `variant`, `variantAndMarking`

### I–P

- Interaction: `height`, `response`, `width`
- Iron golem: `playerCreated`
- Item display: `displayContext`, `itemStack`
- Item entity: `item`
- Item frame: `item`, `rotation`
- Llama: `strength`, `variant`
- Mannequin: `capeEnabled`, `description`, `displayedSkinParts`, `hatEnabled`, `immovable`, `jacketEnabled`, `leftLegEnabled`, `leftSleeveEnabled`, `profile`, `rightLegEnabled`, `rightSleeveEnabled`
- Mooshroom: `variant`
- Ocelot: `trusting`
- Ominous item spawner: `item`
- Painting: `variant`
- Panda: `breedTimer`, `eatTimer`, `hiddenGene`, `mainGene`, `onBack`, `rolling`, `sitting`, `sneezeTimer`, `sneezing`
- Parrot: `color`
- Phantom: `size`
- Pig: `timeToBoost`, `variant`
- Piglin: `baby`, `chargingCrossbow`, `dancing`
- Pillager: `chargingCrossbow`
- Player: `additionalHearts`, `leftShoulderEntityData`, `rightShoulderEntityData`, `score`
- Polar bear: `standingUp`
- Primed TNT: `blockState`, `fuseTime`
- Pufferfish: `state`

### R–T

- Rabbit: `variant`
- Salmon: `size`
- Sheep: `color`, `sheared`
- Shulker: `attachFace`, `color`, `shieldHeight`
- Small fireball: `item`, `shooter`
- Sniffer: `dropSeedAtTick`, `state`
- Snow golem: `hasPumpkinHat`
- Spectral arrow: `shooter`
- Spellcaster illager: `spell`
- Spider: `climbing`
- Strider: `shaking`, `timeToBoost`
- Sulfur cube: `fromBucket`, `maxFuse`
- Tadpole: `ageLocked`
- Text display: `alignLeft`, `alignment`, `alignRight`, `backgroundColor`, `lineWidth`, `seeThrough`, `shadow`, `text`, `textOpacity`, `useDefaultBackground`
- Thrown trident: `hasEnchantmentGlint`, `loyaltyLevel`
- Tropical fish: `variant`
- Turtle: `hasEgg`, `layingEgg`

### V–Z

- Vex: `attacking`
- Villager: `finalized`, `villagerData`
- Warden: `angerLevel`
- Witch: `drinkingPotion`
- Wither: `centerHead`, `centerHeadEntityId`, `invulnerableTime`, `leftHead`, `leftHeadEntityId`, `rightHead`, `rightHeadEntityId`
- Wither skull: `invulnerable`, `shooter`
- Wolf: `angerTime`, `begging`, `collarColor`, `soundVariant`, `variant`
- Zoglin: `baby`
- Zombie: `baby`, `becomingDrowned`, `specialType`
- Zombie nautilus: `variant`
- Zombie villager: `converting`, `finalized`, `villagerData`

## Accepted JSON value formats

Primitive metadata uses normal JSON:

```json
{
  "silent": true,
  "health": 20.0,
  "pose": "standing"
}
```

Enum names ignore capitalization, underscores, spaces, and dashes.

Components use legacy ampersand strings:

```json
{
  "customName": "&6John"
}
```

Points and vectors accept either form:

```json
{
  "translation": [1, 2, 3],
  "homePos": {
    "x": 1,
    "y": 2,
    "z": 3
  }
}
```

Blocks use namespaced IDs:

```json
{
  "carriedBlock": "minecraft:grass_block"
}
```

Item stacks accept a material or an object:

```json
{
  "item": {
    "material": "minecraft:diamond",
    "amount": 4
  }
}
```

Colors accept a packed integer or RGB array:

```json
{
  "color": [255, 100, 50]
}
```

Registry-backed variants use namespaced IDs:

```json
{
  "variant": "minecraft:temperate"
}
```

Multi-argument setters use arrays:

```json
{
  "brightness": [15, 10],
  "variantAndMarking": ["brown", "white"]
}
```

Names are normalized, so `noAi`, `no_ai`, and `no-ai` all find `setNoAi`. The aliases `glowing` and `noGravity` are also accepted.

## Current limitations

- Non-null direct `Entity` references such as `shooter`, `target`, `hookedEntity`, and Wither head references. Where Minestom exposes an `...EntityId` alternative, that integer property works.
- Non-empty `effectParticles` lists. A single `particle` property on area-effect clouds works.
- Mannequin `profile`.
- Villager and zombie-villager `villagerData`.

Everything else in the catalog has a conversion path in the current applier. The implementation is in [`EntityPropertyApplier.java`](C:/Users/mathk/Documents/GitHub/JAB/src/main/java/dev/speedslicer/api/entity/meta/EntityPropertyApplier.java).