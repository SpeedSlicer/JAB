# Item JSON configuration

This reference matches Minestom `2026.07.22-26.2` and JAB item-data version `3`.

## Complete shape

```json
{
  "version": 3,
  "category": "weapon",
  "id": "example_sword",
  "name": "Example Sword",
  "description": ["First lore line", "Second lore line"],
  "material": "minecraft:iron_sword",
  "amount": 1,
  "maxStackSize": 1,
  "displayOptions": {
    "enchantGlint": false,
    "floats": [],
    "flags": [],
    "strings": [],
    "colors": []
  },
  "customData": {},
  "itemBoosts": [
    {"boostType": "DAMAGE", "amount": 5}
  ],
  "components": {},
  "resetComponents": [],
  "removeComponents": [],
  "tags": {}
}
```

| Field | Meaning |
|---|---|
| `version` | Must equal JAB's current item-data version. |
| `category` + `id` | Registry ID, such as `weapon:example_sword`. |
| `name` | Gold custom display name. |
| `description` | Gray lore lines. |
| `material` | Namespaced Minecraft material. |
| `amount` | Initial stack amount; defaults to `1`. |
| `maxStackSize` | Maximum stack size; defaults to `64`. |
| `displayOptions` | Glint and custom-model-data convenience fields. |
| `customData` | Legacy map of raw NBT tags. Prefer `tags`. |
| `itemBoosts` | JAB gameplay boosts: currently `DAMAGE` and `HEALTH`. |
| `components` | Any Minestom item data component. |
| `resetComponents` | Restores components to the material default. |
| `removeComponents` | Removes components, including material defaults. |
| `tags` | Typed persistent server-side tags. |

## Components

Keys may include or omit `minecraft:`. Values use Minestom's JSON codec and field names use `snake_case`.

```json
"components": {
  "rarity": "rare",
  "repair_cost": 3,
  "unbreakable": true,
  "enchantments": {
    "minecraft:sharpness": 5
  }
}
```

- `null` removes a component.
- `unbreakable`, `creative_slot_lock`, `intangible_projectile`, and `glider` use `true` or `false`.
- `map_post_processing` accepts `"lock"` or `"scale"`.
- `components` overrides the convenience fields above.
- `resetComponents` runs after components and tags; `removeComponents` runs last.

### Common recipes

Hide vanilla attribute text without disabling its modifier:

```json
"components": {
  "tooltip_display": {
    "hide_tooltip": false,
    "hidden_components": ["minecraft:attribute_modifiers"]
  }
}
```

Override generated name or lore:

```json
"components": {
  "custom_name": {"text": "Blade", "color": "red", "italic": false},
  "lore": [
    {"text": "Custom lore", "color": "gray", "italic": false}
  ]
}
```

Custom model data:

```json
"components": {
  "custom_model_data": {
    "floats": [1.0],
    "flags": [true],
    "strings": ["jab:blade"],
    "colors": [16711680]
  }
}
```

### Complete component catalog

Simple values:

- Integers: `max_stack_size`, `max_damage`, `damage`, `repair_cost`, `additional_trade_cost`, `map_id`, `ominous_bottle_amplifier`
- Floats: `minimum_attack_charge`, `potion_duration_scale`
- Booleans: `enchantment_glint_override`
- Presence booleans: `unbreakable`, `creative_slot_lock`, `intangible_projectile`, `glider`
- Strings/IDs: `damage_type`, `item_model`, `tooltip_style`, `instrument`, `provides_trim_material`, `jukebox_playable`, `provides_banner_patterns`, `note_block_sound`, `break_sound`
- Text: `custom_name`, `item_name`; text list: `lore`
- Enums/colors: `rarity`, `dye`, `dyed_color`, `map_color`, `map_post_processing`, `base_color`
- String list: `recipes`
- Enchantment maps: `enchantments`, `stored_enchantments`
- Item stack: `use_remainder`, `sulfur_cube_content`; item-stack lists: `charged_projectiles`, `bundle_contents`, `container`

Structured values:

- Identity and display: `custom_data`, `custom_model_data`, `tooltip_display`, `profile`
- Use and consumption: `use_effects`, `food`, `consumable`, `use_cooldown`, `death_protection`
- Combat: `attribute_modifiers`, `damage_resistant`, `weapon`, `attack_range`, `blocks_attacks`, `piercing_weapon`, `kinetic_weapon`, `swing_animation`
- Tools and equipment: `can_place_on`, `can_break`, `tool`, `enchantable`, `equippable`, `repairable`, `trim`
- Maps and potions: `map_decorations`, `potion_contents`, `suspicious_stew_effects`, `lodestone_tracker`
- Books and fireworks: `writable_book_content`, `written_book_content`, `firework_explosion`, `fireworks`
- Entity and block storage: `entity_data`, `bucket_entity_data`, `block_entity_data`, `debug_stick_state`, `banner_patterns`, `pot_decorations`, `block_state`, `bees`
- Predicates and loot: `lock`, `container_loot`

Entity-variant values are normally a namespaced ID, enum name, or dye color:

- `villager/variant`, `wolf/variant`, `wolf/sound_variant`, `wolf/collar`
- `fox/variant`, `salmon/size`, `parrot/variant`
- `tropical_fish/pattern`, `tropical_fish/base_color`, `tropical_fish/pattern_color`
- `mooshroom/variant`, `rabbit/variant`
- `pig/variant`, `pig/sound_variant`
- `cow/variant`, `cow/sound_variant`
- `chicken/variant`, `chicken/sound_variant`
- `zombie_nautilus/variant`, `frog/variant`
- `horse/variant`, `painting/variant`, `llama/variant`, `axolotl/variant`
- `cat/variant`, `cat/sound_variant`, `cat/collar`
- `sheep/color`, `shulker/color`

Complex objects are decoded directly by the pinned Minestom codec. An invalid key or value fails item construction with the component name in the error.

## Tags

```json
"tags": {
  "jab:level": {
    "type": "INTEGER",
    "value": 5,
    "list": false,
    "path": []
  },
  "labels": {
    "type": "STRING",
    "value": ["weapon", "starter"],
    "list": true,
    "path": ["jab"]
  }
}
```

Supported tag types:

| Type | JSON value |
|---|---|
| `BYTE`, `SHORT`, `INTEGER`, `LONG` | Integer |
| `FLOAT`, `DOUBLE` | Number |
| `BOOLEAN` | Boolean |
| `STRING` | String |
| `UUID` | UUID string |
| `COMPONENT` | Adventure text component |
| `ITEM_STACK` | Minestom item-stack object |
| `NBT` | Any JSON value converted to NBT |

Set `list` to `true` for an array of that type. `path` nests the tag inside compounds. A `null` value removes the tag.

## Construction order

1. Material, amount, maximum stack size, name, description, boost lore, and display options.
2. `components`.
3. Legacy `customData`, then typed `tags`.
4. `resetComponents`.
5. `removeComponents`.

Later stages win. For example, removing `minecraft:lore` removes both description and generated boost lore.
