import { defineConfig, Plugin } from 'vite';
import react from '@vitejs/plugin-react';
import tailwindcss from '@tailwindcss/vite';
import fs from 'fs';
import path from 'path';
import AdmZip from 'adm-zip';

function exportZipPlugin(): Plugin {
  return {
    name: 'export-zip-plugin',
    configureServer(server) {
      server.middlewares.use((req, res, next) => {
        if (req.url === '/api/download-zip' || req.url === '/api/export-zip') {
          try {
            const zip = new AdmZip();
            const rootDir = process.cwd();

            const filesToInclude = [
              'src',
              'app',
              'assets',
              'package.json',
              'tsconfig.json',
              'vite.config.ts',
              'index.html',
              'metadata.json',
              '.gitignore',
              '.env.example',
              'README.md',
              'build.gradle.kts',
              'settings.gradle.kts',
              'gradle.properties',
              'gradle'
            ];

            filesToInclude.forEach((item) => {
              const fullPath = path.join(rootDir, item);
              if (fs.existsSync(fullPath)) {
                const stat = fs.statSync(fullPath);
                if (stat.isDirectory()) {
                  zip.addLocalFolder(fullPath, item);
                } else if (stat.isFile()) {
                  zip.addLocalFile(fullPath, '');
                }
              }
            });

            const buffer = zip.toBuffer();
            res.setHeader('Content-Type', 'application/zip');
            res.setHeader(
              'Content-Disposition',
              'attachment; filename="sips-manager-source.zip"'
            );
            res.setHeader('Content-Length', buffer.length.toString());
            res.end(buffer);
            return;
          } catch (err) {
            console.error('Error creating zip:', err);
            res.statusCode = 500;
            res.end('Failed to generate zip');
            return;
          }
        }
        next();
      });
    }
  };
}

export default defineConfig({
  plugins: [react(), tailwindcss(), exportZipPlugin()],
  server: {
    port: 3000,
    host: '0.0.0.0',
  }
});

