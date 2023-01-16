import { GraphicEntityModule } from './entity-module/GraphicEntityModule.js';
import { TooltipModule } from './tooltip-module/TooltipModule.js';

// List of viewer modules that you want to use in your game
export const modules = [
	GraphicEntityModule,
	TooltipModule
];

export const playerColors = [
	'#ff1d5c', // radical red
	'#22a1e4', // curious blue
	'#ff8f16', // west side orange
	'#6ac371', // mantis green
	'#9975e2', // medium purple
	'#3ac5ca', // scooter blue
	'#de6ddf', // lavender pink
	'#ff0000'  // solid red
  ];