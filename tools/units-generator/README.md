# units-generator

Generates the classes in `frc.lib.units` from `src/main/resources/units.json`.

The `generateUnits` Gradle task runs this tool and writes to
`build/generated/sources/units/java`, which is added to the main source set. It runs automatically
before `compileJava`, so `units.json` is the single source of truth for the units library; never
edit the generated files.

```sh
./gradlew generateUnits
```

## Spec format

`units.json` has a `package` field and a `quantities` list. Each quantity entry looks like:

```json
{
    "name": "AngularVelocity",
    "dimension": [0, 0, -1, 0, 0, 0, 0, 1],
    "baseUnitSymbol": "rad/s",
    "units": {
        "RadiansPerSecond": 1,
        "RotationsPerSecond": 6.283185307179586
    },
    "wpiUnits": { "measureType": "AngularVelocity", "unitName": "RadiansPerSecond" }
}
```

* `name` — class name. `AngularVelocity` generates `AngularVelocity.java` and
  `AngularVelocityUnit.java`.
* `dimension` — 8 integer exponents in the order *length, mass, time, current, temperature,
  amount, luminous intensity, angle*. Angle is a base dimension, so e.g. `rad⋅s` ("Anglement")
  works, and torques do **not** accidentally mix with angular momenta.

  The generator derives every cross-quantity operation from these vectors: for each pair of
  quantities `A`, `B`, if `A⋅B` (or `A/B`) matches another entry's dimension vector, `times`/`div`
  methods are emitted on both classes (e.g. `Distance.div(Time) -> Velocity`). Unary
  `reciprocal()` is emitted when a dimension's negation exists (e.g. `Time` ↔ `Frequency`).
* `baseUnitSymbol` — symbol used when logging (`Quantity` writes it to AdvantageKit).
* `units` — constant name → factor in base units. The factor-1.0 entry is the base unit.
  Constant names become `public static final` fields on the unit class in spec order.

Optional fields:

* `wpiUnits` — `{"measureType": "...", "unitName": "...", "constant": "..."}` emits a static
  `ofWpi(org.wpilib.units.measure.<measureType>)` factory that converts via
  `<constant>.of(quantity.in(org.wpilib.units.Units.<unitName>))`. `constant` is the local unit
  constant used to wrap the value; it defaults to `unitName` and must exist in `units`. Use it
  when the local constant's name differs from the WPILib Units field (see `AngularAcceleration`,
  whose WPILib unit is `RadiansPerSecondPerSecond`).
* `imports` — extra imports spliced into both generated classes.
* `quantityCode` — raw method source spliced into the quantity class right after the constructor
  (escape hatch for behavior the spec can't express, e.g. `Angle.toRotation`).
* `unitCode` — raw method source spliced into the unit class right after the `of`/`ofWpi`
  methods, before the unit constants (e.g. `TimeUnit.timestamp`).

Spec errors (duplicate quantities or dimensions, invalid identifiers, non-positive or non-finite
conversion factors, a `wpiUnits.constant` missing from `units`) fail the build with a message
naming the offending entry.

## Non-generated files

These stay hand-written in `src/main/java/frc/lib/units/`:

* `Unit.java` and `Quantity.java` — base classes (`Quantity` handles AdvantageKit logging,
  `Unit` conversion math).
* `Timestamp.java` — a `Time`-adjacent type referenced by `TimeUnit.timestamp(...)`.

Everything else in that package is generated. Add quantities or units by editing `units.json`,
never by editing the generated output.

Note: `units.json` is also packaged into the jar as a resource (it lives under
`src/main/resources`). That is intentional, so the exact spec used to build a robot binary ships
with it; move the file if you don't want that.
