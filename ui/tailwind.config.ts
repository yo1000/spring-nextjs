import type {Config} from "tailwindcss";

const Color = require('color');

function generateShades(baseColor: string) {
  return {
    50: Color(baseColor).lighten(1).hex(),
    100: Color(baseColor).lighten(0.8).hex(),
    200: Color(baseColor).lighten(0.6).hex(),
    300: Color(baseColor).lighten(0.4).hex(),
    400: Color(baseColor).lighten(0.2).hex(),
    500: baseColor,
    600: Color(baseColor).darken(0.2).hex(),
    700: Color(baseColor).darken(0.4).hex(),
    800: Color(baseColor).darken(0.6).hex(),
    900: Color(baseColor).darken(0.8).hex(),
  };
}

const config: Config = {
  content: [
    "./src/pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/components/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      colors: {
        // Configure your color palette here
        neutral: generateShades(`#838383`),
        primary: generateShades(`#c4231b`),
        secondary: generateShades(`#ef9f00`),
        accent: generateShades(`#6bb5ef`),
      },
      backgroundImage: {
        "gradient-radial": "radial-gradient(var(--tw-gradient-stops))",
        "gradient-conic":
          "conic-gradient(from 180deg at 50% 50%, var(--tw-gradient-stops))",
      },
    },
  },
  plugins: [],
} as Config;

export default config;

// /** @type {import('tailwindcss').Config} */
// module.exports = {
//   theme: {
//     colors: {
//       // Configure your color palette here
//       Indigo: `#666666`
//     }
//   }
// }