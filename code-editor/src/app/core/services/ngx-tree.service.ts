import { Injectable } from '@angular/core';
import { MonacoTreeElement } from 'ngx-monaco-tree';

@Injectable({
  providedIn: 'root'
})
export class NgxTreeService {

    rename(path: string, filename: string, localTree: MonacoTreeElement[]) {
        const spited = path.split('/');
        if (spited.length === 1) {
            const file = localTree.find((el) => el.name == path);
            if (filename && file) file.name = filename;
        } else {
            const file = localTree.find((el) => el.name == spited[0]);
            if (!file || !file.content) return;
            this.rename(spited.slice(1).join('/'), filename, file?.content);
        }
    }
  
    remove(path: string, localTree: MonacoTreeElement[]) {
        const spited = path.split('/');
        if (spited.length === 1) {
            const index = localTree.findIndex((el) => el.name == path);
            localTree.splice(index, 1);
        } else {
            const file = localTree.find((el) => el.name == spited[0]);
            if (!file || !file.content) return;
            this.remove(spited.slice(1).join('/'), file?.content);
        }
    }
  
    create(
        type: 'directory' | 'file',
        filename: string,
        path: string,
        localTree: MonacoTreeElement[]
    ) {
        const spited = path.split('/');
        if (!filename) return;
        if (spited.length === 1) {
            const file = localTree.find((el) => el.name == path);
            if (!file) return;
            else if (file.content === undefined) {
                localTree.push({
                    name: filename,
                    content: type === 'directory' ? [] : undefined,
                });
            } else {
                file.content.push({
                    name: filename,
                    content: type === 'directory' ? [] : undefined,
                });
            }
        } else {
            const file = localTree.find((el) => el.name == spited[0]);
            if (!file || !file.content) return;
            this.create(
                type,
                filename,
                spited.slice(1).join('/'),
                file?.content
            );
        }
    }
  
    find(
        path: string,
        localTree: MonacoTreeElement[]
    ): MonacoTreeElement | undefined {
      console.log(path);
        const spited = path.split('/');
        if (spited.length === 1) {
            return localTree.find((el) => el.name == path);
        } else {
            const file = localTree.find((el) => el.name == spited[0]);
            if (!file || !file.content) return;
            return this.find(spited.slice(1).join('/'), file?.content);
        }
    }
}