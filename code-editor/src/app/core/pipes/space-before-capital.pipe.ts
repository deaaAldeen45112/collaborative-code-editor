import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'spaceBeforeCapital'
})
export class SpaceBeforeCapitalPipe implements PipeTransform {

  transform(value: string | undefined): string {
    if (typeof value !== 'string') return ''; 
    return value.replace(/([a-z])([A-Z])/g, '$1 $2');
  }
}


