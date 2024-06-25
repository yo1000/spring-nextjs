import Document, {DocumentContext, Head, Html, Main, NextScript} from 'next/document';

class Doc extends Document {
    static async getInitialProps(ctx: DocumentContext) {
        const initialProps = await Document.getInitialProps(ctx);
        return {...initialProps};
    }

    render() {
        return (
            <Html className={`h-full bg-gray-100`}>
                <Head>
                    {/* Global meta tags, link tags and others */}
                </Head>
                <body className={`h-full`}>
                <Main/>
                <NextScript/>
                </body>
            </Html>
        );
    }
}

export default Doc;
