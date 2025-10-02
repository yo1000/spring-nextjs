import Color from "color";

function generateShades(baseColor) {
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

const config = {
  theme: {
    extend: {
      colors: {
        neutral: generateShades(`#838383`),
        primary: generateShades(`#ef9f00`),
        secondary: generateShades(`#c4231b`),
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
};

export default config;
