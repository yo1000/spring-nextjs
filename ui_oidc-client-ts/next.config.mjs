/** @type {import('next').NextConfig} */
const nextConfig = {
    output: `export`,
    images: {
        unoptimized: true
    },
    reactStrictMode: true,
    exportTrailingSlash: true,
};

export default nextConfig;
