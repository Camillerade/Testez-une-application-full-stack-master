import * as registerCodeCoverageTasks from '@cypress/code-coverage/task';

/**
 * @type {Cypress.PluginConfig}
 */
export default (on, config) => {
  registerCodeCoverageTasks(on, config);
  return config;
};
